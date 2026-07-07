# test-estudante.ps1
$baseUriUsuario = "http://localhost:8080/api/usuarios"
$baseUriEstudante = "http://localhost:8080/api/estudantes"

$cpf = 33333333301
$matricula = "3330001"

Write-Host "`n=== TESTE ESTUDANTE ===" -ForegroundColor Cyan

# --- 1. Criar usuário (obrigatório para o estudante) ---
try {
    Write-Host "Criando usuario com CPF $cpf (necessario para o estudante)..."
    $bodyUser = @{
        cpf = $cpf
        nome = "Usuario Teste Java"
        dataNascimento = "2000-01-01"
        email = @("usuario.teste@email.com")
        telefone = @("79999999999")
        login = "usuario.teste.java"
        senha = "123456"
    } | ConvertTo-Json

    $user = Invoke-RestMethod -Uri $baseUriUsuario -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyUser
    Write-Host "Usuario criado: CPF $($user.cpf)" -ForegroundColor Green
} catch {
    Write-Host "Falha ao criar usuário: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# --- 2. Criar estudante ---
try {
    $bodyCreate = @{
        matricula   = $matricula
        cpf         = $cpf
        mc          = 85
        anoIngresso = 2025
    } | ConvertTo-Json

    Write-Host "Criando estudante $matricula..."
    $responseCreate = Invoke-RestMethod -Uri $baseUriEstudante -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyCreate
    Write-Host "Criado: $($responseCreate | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao criar estudante: $($_.Exception.Message)" -ForegroundColor Red
    # Limpa o usuário criado
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete -ErrorAction SilentlyContinue
    exit 1
}

# --- 3. Listar todos ---
$all = Invoke-RestMethod -Uri $baseUriEstudante -Method Get
Write-Host "Total de estudantes: $($all.Count)" -ForegroundColor Yellow

# --- 4. Buscar por matrícula ---
try {
    $student = Invoke-RestMethod -Uri "$baseUriEstudante/$matricula" -Method Get
    Write-Host "Estudante encontrado: CPF $($student.cpf), MC $($student.mc)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao buscar estudante: $($_.Exception.Message)" -ForegroundColor Red
}

# --- 5. Atualizar ---
$bodyUpdate = @{
    matricula   = $matricula
    cpf         = $cpf
    mc          = 90
    anoIngresso = 2026
} | ConvertTo-Json

try {
    Write-Host "Atualizando estudante..."
    $responseUpdate = Invoke-RestMethod -Uri "$baseUriEstudante/$matricula" -Method Put -ContentType "application/json" -Body $bodyUpdate
    Write-Host "Atualizado: MC $($responseUpdate.mc), Ano $($responseUpdate.anoIngresso)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao atualizar: $($_.Exception.Message)" -ForegroundColor Red
}

# --- 6. Deletar estudante ---
try {
    Write-Host "Deletando estudante..."
    Invoke-RestMethod -Uri "$baseUriEstudante/$matricula" -Method Delete | Out-Null
    Write-Host "Estudante removido." -ForegroundColor Green
} catch {
    Write-Host "Erro ao deletar estudante: $($_.Exception.Message)" -ForegroundColor Red
}

# --- 7. Deletar usuário (limpeza) ---
try {
    Write-Host "Removendo usuario..."
    Invoke-RestMethod -Uri "$baseUriUsuario/$cpf" -Method Delete | Out-Null
    Write-Host "Usuario removido." -ForegroundColor Green
} catch {
    Write-Host "Nao foi possivel remover usuario: $($_.Exception.Message)" -ForegroundColor Yellow
}

Write-Host "=== FIM TESTE ESTUDANTE ===" -ForegroundColor Cyan