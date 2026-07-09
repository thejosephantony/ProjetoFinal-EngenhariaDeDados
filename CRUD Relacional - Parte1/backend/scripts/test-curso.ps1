# test-curso.ps1
# Testa CRUD de curso contra a API Spring Boot (localhost:8080)
# Uso: .\test-curso.ps1

$baseUri = "http://localhost:8080/api/cursos"
$nomeCurso = "Curso Teste Java"
$idCursoCriado = $null

Write-Host "`n=== TESTE CURSO ===" -ForegroundColor Cyan

# 1. Criar curso
$bodyCreate = @{
    nome   = $nomeCurso
    grau   = "Bacharelado"
    turno  = "Matutino"
    campus = "Campus Aracaju"
    nivel  = $null
} | ConvertTo-Json

Write-Host "Criando curso '$nomeCurso'..."
try {
    $responseCreate = Invoke-RestMethod -Uri $baseUri -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyCreate
    $idCursoCriado = $responseCreate.idCurso
    Write-Host "Criado com ID: $idCursoCriado" -ForegroundColor Green
    Write-Host "   $($responseCreate | ConvertTo-Json)"
} catch {
    Write-Host "Erro ao criar: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. Listar todos
Write-Host "`nListando todos os cursos..."
try {
    $responseGetAll = Invoke-RestMethod -Uri $baseUri -Method Get
    Write-Host "Total encontrado: $($responseGetAll.Count)" -ForegroundColor Yellow
} catch {
    Write-Host "Erro ao listar: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. Buscar por ID
Write-Host "`nBuscando curso pelo ID $idCursoCriado..."
try {
    $responseGetOne = Invoke-RestMethod -Uri "$baseUri/$idCursoCriado" -Method Get
    Write-Host "Encontrado: $($responseGetOne.nome)" -ForegroundColor Green
    Write-Host "   Grau: $($responseGetOne.grau), Turno: $($responseGetOne.turno), Campus: $($responseGetOne.campus)"
} catch {
    Write-Host "Erro ao buscar: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. Atualizar
$bodyUpdate = @{
    nome   = "$nomeCurso (Atualizado)"
    turno  = "Noturno"
} | ConvertTo-Json
Write-Host "`nAtualizando curso (nome e turno)..."
try {
    $responseUpdate = Invoke-RestMethod -Uri "$baseUri/$idCursoCriado" -Method Put -ContentType "application/json" -Body $bodyUpdate
    Write-Host "Atualizado: $($responseUpdate.nome) - Turno: $($responseUpdate.turno)" -ForegroundColor Green
} catch {
    Write-Host "Erro ao atualizar: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. Deletar
Write-Host "`nDeletando curso ID $idCursoCriado..."
try {
    $responseDelete = Invoke-RestMethod -Uri "$baseUri/$idCursoCriado" -Method Delete
    Write-Host "Curso removido com sucesso." -ForegroundColor Green
} catch {
    Write-Host "Erro ao deletar: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== FIM TESTE CURSO ===" -ForegroundColor Cyan