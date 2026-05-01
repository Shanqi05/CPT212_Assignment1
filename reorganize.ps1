# PowerShell script to reorganize CPT212_Part2 files
# Save as: reorganize.ps1
# Run as: .\reorganize.ps1

Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         CPT212_Part2 - Automatic File Reorganization          ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$root = "C:\Users\user\Desktop\CPT212_Part2"
$srcPath = Join-Path $root "src"

# Verify we're in the correct location
if (!(Test-Path $root)) {
    Write-Host "❌ Error: Project folder not found at $root" -ForegroundColor Red
    exit 1
}

Write-Host "📁 Creating directory structure..." -ForegroundColor Yellow
$directories = @(
    "src\algorithms",
    "src\utilities",
    "src\analysis",
    "src\visualization",
    "output\small_numbers",
    "output\large_numbers",
    "output\merged",
    "documentation",
    "build"
)

foreach ($dir in $directories) {
    $path = Join-Path $root $dir
    if (!(Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force | Out-Null
        Write-Host "  ✓ Created: $dir" -ForegroundColor Green
    } else {
        Write-Host "  ✓ Already exists: $dir" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "📦 Moving source files..." -ForegroundColor Yellow

# Move source files
$sourceFiles = @{
    "algorithms" = @(
        "SimpleMultiplication.java",
        "Karatsuba.java",
        "OperationCounter.java"
    )
    "utilities" = @(
        "DataGenerator.java"
    )
    "analysis" = @(
        "ComparisonAnalysis.java",
        "AnalysisReport.java",
        "LargeNumberComparison.java"
    )
    "visualization" = @(
        "GraphComparison.java",
        "Karatsuba_graph.java"
    )
}

foreach ($category in $sourceFiles.Keys) {
    foreach ($file in $sourceFiles[$category]) {
        $source = Join-Path $srcPath $file
        $dest = Join-Path $srcPath $category
        
        if (Test-Path $source) {
            Move-Item $source $dest -Force
            Write-Host "  ✓ Moved: $file → src\$category\" -ForegroundColor Green
        }
    }
}

Write-Host ""
Write-Host "📊 Organizing output files..." -ForegroundColor Yellow

# Move output files - small numbers
$smallNumberFiles = @(
    "comparison_results.csv",
    "comparison_graph.png",
    "karatsuba_data.csv",
    "karatsuba_plot.png",
    "comparison.png",
    "ALGORITHM_ANALYSIS_REPORT.md"
)

foreach ($file in $smallNumberFiles) {
    $source = Join-Path $root $file
    if (Test-Path $source) {
        Move-Item $source (Join-Path $root "output\small_numbers\") -Force
        Write-Host "  ✓ Moved: $file → output\small_numbers\" -ForegroundColor Green
    }
}

# Move output files - large numbers
$largeNumberFiles = @(
    "large_comparison_results.csv",
    "large_comparison_graph.png"
)

foreach ($file in $largeNumberFiles) {
    $source = Join-Path $root $file
    if (Test-Path $source) {
        Move-Item $source (Join-Path $root "output\large_numbers\") -Force
        Write-Host "  ✓ Moved: $file → output\large_numbers\" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "📚 Organizing documentation..." -ForegroundColor Yellow

# Move documentation files
$docFiles = @(
    "README.md",
    "IMPLEMENTATION_GUIDE.md",
    "FILE_STRUCTURE.md",
    "KARATSUBA_VERIFICATION.md",
    "FILE_REORGANIZATION_GUIDE.md"
)

foreach ($file in $docFiles) {
    $source = Join-Path $root $file
    if (Test-Path $source) {
        Move-Item $source (Join-Path $root "documentation\") -Force
        Write-Host "  ✓ Moved: $file → documentation\" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "🔧 Compilation test..." -ForegroundColor Yellow

# Try to compile
cd $srcPath

# Compile with proper classpath
Write-Host "  ↳ Compiling all Java files..." -ForegroundColor Gray
$output = javac algorithms\*.java utilities\*.java 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "  ✓ Compilation successful!" -ForegroundColor Green
    
    # Move .class files to build directory
    Get-Item algorithms\*.class | Move-Item -Destination ..\build\ -Force 2>$null
    Get-Item utilities\*.class | Move-Item -Destination ..\build\ -Force 2>$null
    Get-Item analysis\*.class | Move-Item -Destination ..\build\ -Force 2>$null
    Get-Item visualization\*.class | Move-Item -Destination ..\build\ -Force 2>$null
    Write-Host "  ✓ Compiled classes moved to build\ folder" -ForegroundColor Green
} else {
    Write-Host "  ⚠ Compilation with warnings (may be normal)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                  ✓ REORGANIZATION COMPLETE!                    ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Summary of folder structure:" -ForegroundColor Cyan
Write-Host "  src/"
Write-Host "    ├─ algorithms/         (Karatsuba, SimpleMultiplication, OperationCounter)"
Write-Host "    ├─ utilities/          (DataGenerator)"
Write-Host "    ├─ analysis/           (Comparison, Report, LargeNumberComparison)"
Write-Host "    └─ visualization/      (Graph generation)"
Write-Host "  output/"
Write-Host "    ├─ small_numbers/      (Results for n=1-100)"
Write-Host "    ├─ large_numbers/      (Results for n=10-1000)"
Write-Host "    └─ merged/             (Combined analysis)"
Write-Host "  documentation/           (All .md files)"
Write-Host "  build/                   (Compiled .class files)"
Write-Host ""

Write-Host "🚀 Next steps:" -ForegroundColor Cyan
Write-Host "  1. Run new programs:"
Write-Host "     cd C:\Users\user\Desktop\CPT212_Part2\src"
Write-Host "     java -cp . algorithms.Karatsuba"
Write-Host "     java -cp . visualization.GraphComparison"
Write-Host "     java -cp . analysis.LargeNumberComparison"
Write-Host ""
Write-Host "  2. View results:"
Write-Host "     • output\small_numbers\  - Results for normal range"
Write-Host "     • output\large_numbers\  - Results for large numbers"
Write-Host "     • documentation\         - Markdown documentation"
Write-Host ""

Write-Host "✅ Reorganization completed successfully!" -ForegroundColor Green
