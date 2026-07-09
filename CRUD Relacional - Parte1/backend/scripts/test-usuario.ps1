# test-usuario.ps1
$baseUri = "http://localhost:8080/api/usuarios"
$cpf = 33333333301
$emailList = @("usuario.teste@email.com")
$telefoneList = @("79999999999")

Write-Host "`n=== TESTE USUARIO ===" -ForegroundColor Cyan

# 1. Criar
$bodyCreate = @{
    cpf = $cpf
    nome = "Usuario Teste Java"
    dataNascimento = "2000-01-15"
    email = $emailList
    telefone = $telefoneList
    login = "usuario.teste.java"
    senha = "123456"
} | ConvertTo-Json

Write-Host "Criando usuario..."
$responseCreate = Invoke-RestMethod -Uri $baseUri -Method Post -ContentType "application/json; charset=utf-8" -Body $bodyCreate
Write-Host "Criado: $($responseCreate | ConvertTo-Json)" -ForegroundColor Green

# 2. Listar
$all = Invoke-RestMethod -Uri $baseUri -Method Get
Write-Host "Total de usuários: $($all.Count)" -ForegroundColor Yellow

# 3. Buscar por CPF
$user = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Get
Write-Host "Usuário encontrado: $($user.nome)" -ForegroundColor Green

# 4. Atualizar (envia o objeto completo com nome alterado)
$bodyUpdate = @{
    cpf = $cpf
    nome = "Usuario Teste Java Atualizado"
    dataNascimento = "2000-01-15"
    email = $emailList
    telefone = $telefoneList
    login = "usuario.teste.java"
    senha = "123456"
} | ConvertTo-Json

Write-Host "Atualizando usuario..."
$responseUpdate = Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Put -ContentType "application/json" -Body $bodyUpdate
Write-Host "Atualizado: $($responseUpdate.nome)" -ForegroundColor Green

# 5. Deletar
Write-Host "Deletando usuario..."
Invoke-RestMethod -Uri "$baseUri/$cpf" -Method Delete | Out-Null
Write-Host "Usuário removido." -ForegroundColor Green

Write-Host "=== FIM TESTE USUARIO ===" -ForegroundColor Cyan