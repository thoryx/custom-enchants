<#
Simple build helper for Custom Enchants

Usage:
  .\build.ps1                # builds with mvn
  .\build.ps1 -CopyTo PATH   # builds and copies the generated JAR to PATH
#>

param(
    [string]$CopyTo
)

function Get-JavaMajorVersion {
    try {
        $out = & java -version 2>&1 | Select-Object -First 1
        if (-not $out) { return $null }
        if ($out -match 'version "?([0-9]+)') { return [int]$Matches[1] }
    } catch {
        return $null
    }
    return $null
}

Write-Host 'Checking java...'
$javaVer = Get-JavaMajorVersion
if (-not $javaVer) {
    Write-Host 'No java executable found in PATH. Install JDK 17+ or set JAVA_HOME.' -ForegroundColor Red
    exit 1
}

if ($javaVer -lt 17) {
    Write-Host "Detected Java major version $javaVer. Java 17 or newer is required." -ForegroundColor Yellow
    Write-Host 'Set JAVA_HOME for this session (example):' -ForegroundColor Yellow
    Write-Host "$env:JAVA_HOME = 'C:\Path\To\jdk-17'" -ForegroundColor Yellow
    Write-Host "$env:Path = \"$env:JAVA_HOME\\bin;$env:Path\"" -ForegroundColor Yellow
    exit 1
}

Write-Host "Java $javaVer detected — proceeding to build."

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host 'Maven (mvn) not found. Please install Maven and add it to PATH.' -ForegroundColor Red
    exit 1
}

Write-Host 'Running: mvn -DskipTests package'
$proc = Start-Process mvn -ArgumentList '-DskipTests','package' -NoNewWindow -Wait -PassThru
if ($proc.ExitCode -ne 0) {
    Write-Host "Maven build failed (exit code $($proc.ExitCode))." -ForegroundColor Red
    exit $proc.ExitCode
}

Write-Host 'Build complete. Locating JAR in target/'
$targetDir = Join-Path -Path (Get-Location) -ChildPath 'target'
if (-not (Test-Path $targetDir)) { Write-Host 'target/ not found.' -ForegroundColor Red; exit 1 }

$jar = Get-ChildItem -Path $targetDir -Filter '*.jar' | Where-Object { $_.Name -notlike '*-sources*' -and $_.Name -notlike '*-javadoc*' } | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if (-not $jar) { Write-Host 'No JAR found in target/.' -ForegroundColor Red; exit 1 }

Write-Host "Found JAR: $($jar.Name)"

if ($CopyTo) {
    if (-not (Test-Path $CopyTo)) {
        Write-Host "Destination path '$CopyTo' does not exist." -ForegroundColor Red
        exit 1
    }
    $dest = Join-Path -Path $CopyTo -ChildPath $jar.Name
    Copy-Item -Path $jar.FullName -Destination $dest -Force
    Write-Host "Copied $($jar.Name) -> $dest"
}

Write-Host 'Done.'
