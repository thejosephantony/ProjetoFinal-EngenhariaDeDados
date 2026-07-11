# test-nosql.ps1 - Teste CRUD NoSQL
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$baseUri = "http://localhost:8081/api/nosql/estudantes"
$cpf = "33333333302"
$matricula = "999001"

Write-Host ""
Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host "    TESTE CRUD NOSQL - ESTUDANTE (MongoDB)" -ForegroundColor Cyan
Write-Host "    CPF: $cpf  |  Matricula: $matricula" -ForegroundColor Cyan
Write-Host "=============================================================" -ForegroundColor Cyan

# 1. CRIAR (POST)
Write-Host ""
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
    $responseCreate = Invoke-RestMethod -Uri $baseUri -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyCreate
    Write-Host "SUCESSO: Estudante criado (ID: $($responseCreate.id))" -ForegroundColor Green
    Write-Host "  Nome: $($responseCreate.nome), MC: $($responseCreate.mc)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. LISTAR TODOS (GET)
Write-Host ""
Write-Host "[2] LISTANDO TODOS OS ESTUDANTES..." -ForegroundColor Yellow

try {
    $responseList = Invoke-RestMethod -Uri $baseUri -Method Get
    Write-Host "Total de estudantes: $($responseList.Count)" -ForegroundColor Cyan
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 3. BUSCAR POR CPF (GET)
Write-Host ""
Write-Host "[3] BUSCANDO ESTUDANTE PELO CPF: $cpf..." -ForegroundColor Yellow

try {
    $responseGet = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Get
    Write-Host "Estudante encontrado:" -ForegroundColor Green
    Write-Host "  Nome: $($responseGet.nome)" -ForegroundColor Green
    Write-Host "  Matricula: $($responseGet.matricula)" -ForegroundColor Green
    Write-Host "  MC: $($responseGet.mc)" -ForegroundColor Green
    Write-Host "  Ano Ingresso: $($responseGet.anoIngresso)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 4. ATUALIZAR (PUT)
Write-Host ""
Write-Host "[4] ATUALIZANDO ESTUDANTE (MC=90, Ano=2026)..." -ForegroundColor Yellow

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
    $responseUpdate = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Put -ContentType "application/json; charset=utf-8" -Body $bodyUpdate
    Write-Host "SUCESSO: Estudante atualizado!" -ForegroundColor Green
    Write-Host "  MC: $($responseUpdate.mc) (antes era 85)" -ForegroundColor Green
    Write-Host "  Ano Ingresso: $($responseUpdate.anoIngresso) (antes era 2025)" -ForegroundColor Green
} catch {
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 5. DELETAR (DELETE)
Write-Host ""
Write-Host "[5] DELETANDO ESTUDANTE (CPF: $cpf)..." -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Delete
    Write-Host "SUCESSO: Estudante removido!" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "AVISO: Estudante ja havia sido removido (404)" -ForegroundColor Yellow
    } else {
        Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

# 6. CONFIRMAR REMOCAO
Write-Host ""
Write-Host "[6] CONFIRMANDO REMOCAO..." -ForegroundColor Yellow

try {
    $responseConfirm = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Get
    Write-Host "ATENCAO: Estudante ainda existe!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "CONFIRMADO: Estudante nao encontrado (404) - Remocao bem-sucedida!" -ForegroundColor Green
    } else {
        Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=============================================================" -ForegroundColor Cyan
Write-Host "    TESTE NOSQL CONCLUIDO COM SUCESSO!" -ForegroundColor Cyan
Write-Host "=============================================================" -ForegroundColor Cyan