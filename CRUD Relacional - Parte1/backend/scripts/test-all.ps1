# test-all.ps1
Write-Host "Iniciando bateria de testes..." -ForegroundColor Magenta

.\test-usuario.ps1
.\test-estudante.ps1
.\test-curso.ps1
.\test-vinculo.ps1

Write-Host "Todos os testes concluídos." -ForegroundColor Magenta