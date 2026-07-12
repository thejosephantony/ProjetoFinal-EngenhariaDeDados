# test-nosql-prints.ps1
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$baseUri = "http://localhost:8081/api/nosql/estudantes"
$cpf = "33333333302"
$matricula = "999001"

Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host "TESTE CRUD NOSQL" -ForegroundColor Cyan
Write-Host "CPF: $cpf  |  Matricula: $matricula" -ForegroundColor Cyan
Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Abra o MongoDB Compass na colecao 'estudantes'." -ForegroundColor Yellow
Read-Host "Pressione Enter para comecar (certifique-se que a colecao esta vazia)"

# 1. CREATE
Write-Host "[1] CRIANDO ESTUDANTE..." -ForegroundColor Yellow
$bodyCreate = @{
    cpf = $cpf
    nome = "Joao NoSQL"
    dataNascimento = "2000-01-15"
    email = @("joao.nosql@email.com")
    telefone = @("79999999999")
    login = "joao.nosql"
    senha = "123456"
    matricula = $matricula
    mc = 85
    anoIngresso = 2025
    cursos = @(
        @{
            cursoId = "curso_001"
            nome = "Engenharia de Dados"
            grau = "Bacharelado"
            turno = "Noturno"
            campus = "Aracaju"
            nivel = "Graduacao"
            vinculo = @{
                dataEntrada = "2025-02-01"
                status = "Ativo"
                dataSaida = $null
            }
        }
    )
} | ConvertTo-Json -Depth 5

try {
    $r = Invoke-RestMethod -Uri $baseUri -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyCreate
    Write-Host "SUCESSO: Criado (ID: $($r.id))" -ForegroundColor Green
    Write-Host "Nome: $($r.nome), MC: $($r.mc)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Atualize o Compass (Refresh) e verifique." -ForegroundColor Yellow
Read-Host "Pressione Enter para continuar"

# 2. LIST ALL
Write-Host "[2] LISTANDO TODOS..." -ForegroundColor Yellow
try {
    $list = Invoke-RestMethod -Uri $baseUri -Method Get
    Write-Host "Total: $($list.Count)" -ForegroundColor Cyan
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. GET BY CPF
Write-Host "[3] BUSCANDO POR CPF..." -ForegroundColor Yellow
try {
    $get = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Get
    Write-Host "Encontrado: Nome $($get.nome), MC $($get.mc), Ano $($get.anoIngresso)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. UPDATE (PUT)
Write-Host "[4] ATUALIZANDO (MC=90, Ano=2026)..." -ForegroundColor Yellow
$bodyUpdate = @{
    cpf = $cpf
    nome = "Joao NoSQL Atualizado"
    dataNascimento = "2000-01-15"
    email = @("joao.nosql@email.com")
    telefone = @("79999999999")
    login = "joao.nosql"
    senha = "123456"
    matricula = $matricula
    mc = 90
    anoIngresso = 2026
    cursos = @(
        @{
            cursoId = "curso_001"
            nome = "Engenharia de Dados"
            grau = "Bacharelado"
            turno = "Noturno"
            campus = "Aracaju"
            nivel = "Graduacao"
            vinculo = @{
                dataEntrada = "2025-02-01"
                status = "Ativo"
                dataSaida = $null
            }
        }
    )
} | ConvertTo-Json -Depth 5

try {
    $u = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Put -ContentType "application/json; charset=utf-8" -Body $bodyUpdate
    Write-Host "SUCESSO: Atualizado! MC: $($u.mc) (antes 85), Ano: $($u.anoIngresso) (antes 2025)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Atualize o Compass (Refresh) e verifique o documento atualizado." -ForegroundColor Yellow
Read-Host "Pressione Enter para continuar"

# 5. DELETE
Write-Host "[5] DELETANDO..." -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Delete
    Write-Host "SUCESSO: Removido!" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "AVISO: Ja removido (404)" -ForegroundColor Yellow
    } else {
        Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "Atualize o Compass (Refresh) e verifique que o documento sumiu." -ForegroundColor Yellow
Read-Host "Pressione Enter para continuar"

# 6. CONFIRMAR REMOCAO
Write-Host "[6] CONFIRMANDO REMOCAO..." -ForegroundColor Yellow
try {
    $confirm = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Get
    Write-Host "ATENCAO: Estudante ainda existe!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "CONFIRMADO: Nao encontrado (404) - Remocao bem-sucedida!" -ForegroundColor Green
    } else {
        Write-Host "ERRO INESPERADO: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host "TESTE CONCLUIDO" -ForegroundColor Cyan
Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host ""