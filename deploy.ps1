param(
    [string]$DbUrl      = "jdbc:postgresql://localhost:5432/resume_analyzer",
    [string]$DbUser     = "postgres",
    [string]$DbPassword = "root"
)

$ErrorActionPreference = "Stop"
$Root = $PSScriptRoot

function Step($n, $msg) { Write-Host "`n[$n/3] $msg" -ForegroundColor Cyan }
function OK($msg)        { Write-Host "  OK: $msg"  -ForegroundColor Green }
function Fail($msg)      { Write-Host "  FAIL: $msg" -ForegroundColor Red; exit 1 }

Write-Host ""
Write-Host "=============================================" -ForegroundColor Magenta
Write-Host "  Smart Resume Analyzer — Production Deploy  " -ForegroundColor Magenta
Write-Host "=============================================" -ForegroundColor Magenta

# ── 1. Build React frontend ───────────────────────────────────────────────────
Step 1 "Building React frontend..."

$frontendDir = Join-Path $Root "frontend"
Push-Location $frontendDir
try {
    npm install --silent
    if ($LASTEXITCODE -ne 0) { Fail "npm install failed" }

    npm run build
    if ($LASTEXITCODE -ne 0) { Fail "npm run build failed" }
} finally { Pop-Location }

# Copy dist → backend static resources
$distDir    = Join-Path $frontendDir "dist"
$staticDir  = Join-Path $Root "backend\src\main\resources\static"

if (Test-Path $staticDir) { Remove-Item -Recurse -Force $staticDir }
New-Item -ItemType Directory -Path $staticDir -Force | Out-Null
Copy-Item -Recurse -Force "$distDir\*" $staticDir
OK "Frontend built and copied to backend/src/main/resources/static"

# ── 2. Package Spring Boot JAR ────────────────────────────────────────────────
Step 2 "Packaging Spring Boot JAR (this may take a minute)..."

$backendDir = Join-Path $Root "backend"
Push-Location $backendDir
try {
    mvn package -DskipTests -q
    if ($LASTEXITCODE -ne 0) { Fail "mvn package failed" }
} finally { Pop-Location }

$jar = Join-Path $backendDir "target\smart-resume-analyzer-1.0.0.jar"
if (-not (Test-Path $jar)) { Fail "JAR not found at $jar" }
OK "JAR built: $jar"

# ── 3. Launch application ─────────────────────────────────────────────────────
Step 3 "Starting application..."

Write-Host ""
Write-Host "  URL  : http://localhost:8080" -ForegroundColor Yellow
Write-Host "  DB   : $DbUrl"               -ForegroundColor Yellow
Write-Host "  Press Ctrl+C to stop"         -ForegroundColor Yellow
Write-Host ""

java `
  -Dspring.datasource.url="$DbUrl" `
  -Dspring.datasource.username="$DbUser" `
  -Dspring.datasource.password="$DbPassword" `
  -jar $jar
