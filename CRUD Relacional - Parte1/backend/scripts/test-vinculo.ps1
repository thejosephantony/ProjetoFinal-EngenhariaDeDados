# test-vinculo.ps1
$baseUriUsuario = "http://localhost:8080/api/usuarios"
$baseUriEst = "http://localhost:8080/api/estudantes"
$baseUriCurso = "http://localhost:8080/api/cursos"
$baseUriVinculo = "http://localhost:8080/api/vinculos"

$cpf = 33333333303
$matricula = "999002"
$nomeCurso = "Curso Temp Vinculo $(Get-Date -Format 'yyyyMMddHHmmss')"   # nome único

Write-Host "`n=== TESTE VINCULO ===" -ForegroundColor Cyan

# --- 1. Criar usuário ---
try {
    Write-Host "Criando usuario com CPF $cpf (necessario para o estudante)..."
    $bodyUser = @{
        cpf = $cpf
        nome = "Usuario Teste Java"
        dataNascimento = "2000-01-01"
        email = @("vinculo.base@email.com")
        telefone = @("79999999999")
        login = "vinculo.base"
        senha = "123456"
    } | ConvertTo-Json

    $user = Invoke-RestMethod -Uri $baseUriUsuario -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyUser
    Write-Host "Usuário criado: CPF $($user.cpf)" -ForegroundColor Green
} catch {
    Write-Host "Falha ao criar usuário: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# --- 2. Criar estudante ---
try {
    Write-Host "Criando estudante temporario..."
    $bodyEst = @{
        matricula   = $matricula
        cpf         = $cpf
        mc          = 70
        anoIngresso = 2025
    } | ConvertTo-Json

    $estudante = Invoke-RestMethod -Uri $baseUriEst -Method Post -ContentType "application/json" -Body $bodyEst
    Write-Host "Estudante criado: $matricula" -ForegroundColor Green
} catch {
    Write-Host "Falha ao criar estudante: $($_.Exception.Message)" -ForegroundColor Red
    # Exibe o corpo do erro
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $errorBody = $reader.ReadToEnd()
        Write-Host "Detalhe: $errorBody" -ForegroundColor Yellow
    }
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete -ErrorAction SilentlyContinue
    exit 1
}

# --- 3. Criar curso (usando valores que funcionaram no teste de curso) ---
try {
    Write-Host "Criando curso temporário..."
    $bodyCurso = @{
        nome   = $nomeCurso
        grau   = "Bacharelado"          # <-- mesmo do teste de curso
        turno  = "Matutino"             # <-- mesmo do teste de curso
        campus = "Campus Itabaiana"
        nivel  = $null                  # <-- null (como no teste de curso)
    } | ConvertTo-Json

    $curso = Invoke-RestMethod -Uri $baseUriCurso -Method Post -ContentType "application/json" -Body $bodyCurso
    $idCurso = $curso.idCurso
    Write-Host "Curso criado com ID: $idCurso" -ForegroundColor Green
} catch {
    Write-Host "Falha ao criar curso: $($_.Exception.Message)" -ForegroundColor Red
    # Exibe o corpo do erro
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $errorBody = $reader.ReadToEnd()
        Write-Host "Detalhe: $errorBody" -ForegroundColor Yellow
    }
    # Limpa dados criados
    Invoke-RestMethod -Uri "$baseUriEst/$matricula" -Method Delete -ErrorAction SilentlyContinue
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete -ErrorAction SilentlyContinue
    exit 1
}

# --- 4. Criar vínculo ---
try {
    $bodyVinculo = @{
        matriculaEstudante = $matricula
        cursoId            = $idCurso
        dataEntrada        = "2025-02-01"
        status             = "Ativo"
        dataSaida          = $null
    } | ConvertTo-Json

    Write-Host "Criando vinculo..."
    $vinculo = Invoke-RestMethod -Uri $baseUriVinculo -Method Post -ContentType "application/json" -Body $bodyVinculo
    $idVinculo = $vinculo.idVinculo
    Write-Host "Vinculo criado com ID: $idVinculo" -ForegroundColor Green
} catch {
    Write-Host "Falha ao criar vinculo: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $errorBody = $reader.ReadToEnd()
        Write-Host "Detalhe: $errorBody" -ForegroundColor Yellow
    }
    # Limpa curso, estudante, usuário
    Invoke-RestMethod -Uri "$baseUriCurso/$idCurso" -Method Delete -ErrorAction SilentlyContinue
    Invoke-RestMethod -Uri "$baseUriEst/$matricula" -Method Delete -ErrorAction SilentlyContinue
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete -ErrorAction SilentlyContinue
    exit 1
}

# --- 5. Listar todos os vínculos ---
$all = Invoke-RestMethod -Uri $baseUriVinculo -Method Get
Write-Host "Total de vinculos: $($all.Count)" -ForegroundColor Yellow

# --- 6. Buscar por ID ---
try {
    $v = Invoke-RestMethod -Uri "$baseUriVinculo/$idVinculo" -Method Get
    Write-Host "Vinculo encontrado: Status $($v.status)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao buscar vinculo: $($_.Exception.Message)" -ForegroundColor Red
}

# --- 7. Atualizar ---
$bodyUpdate = @{
    matriculaEstudante = $matricula
    cursoId            = $idCurso
    dataEntrada        = "2025-02-01"    
    status             = "Cancelada"     
    dataSaida          = "2026-07-04"
} | ConvertTo-Json

# DEBUG: exibe o corpo para ver se não está vazio
Write-Host "Corpo do PUT: $bodyUpdate" -ForegroundColor Cyan

try {
    $vUpdated = Invoke-RestMethod -Uri "$baseUriVinculo/$idVinculo" -Method Put -ContentType "application/json" -Body $bodyUpdate
    Write-Host "Atualizado: Status $($vUpdated.status), Saida $($vUpdated.dataSaida)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao atualizar: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $errorBody = $reader.ReadToEnd()
        Write-Host "Detalhe: $errorBody" -ForegroundColor Yellow
    }
}

# --- 8. Deletar vínculo ---
try {
    Write-Host "Deletando vinculo..."
    Invoke-RestMethod -Uri "$baseUriVinculo/$idVinculo" -Method Delete | Out-Null
    Write-Host "Vinculo removido." -ForegroundColor Green
} catch {
    Write-Host "Erro ao deletar vinculo: $($_.Exception.Message)" -ForegroundColor Red
}

# --- 9. Limpeza: deletar curso, estudante e usuário ---
try {
    Write-Host "Removendo curso..."
    Invoke-RestMethod -Uri "$baseUriCurso/$idCurso" -Method Delete | Out-Null
    Write-Host "Curso removido." -ForegroundColor Green
} catch {
    Write-Host "Nao foi possível remover curso: $($_.Exception.Message)" -ForegroundColor Yellow
}

try {
    Write-Host "Removendo estudante..."
    Invoke-RestMethod -Uri "$baseUriEst/$matricula" -Method Delete | Out-Null
    Write-Host "Estudante removido." -ForegroundColor Green
} catch {
    Write-Host "Nao foi possível remover estudante: $($_.Exception.Message)" -ForegroundColor Yellow
}

try {
    Write-Host "Removendo usuario base..."
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete | Out-Null
    Write-Host "Usuario removido." -ForegroundColor Green
} catch {
    Write-Host "Nao foi possível remover usuário: $($_.Exception.Message)" -ForegroundColor Yellow
}

Write-Host "=== FIM TESTE VINCULO ===" -ForegroundColor Cyan