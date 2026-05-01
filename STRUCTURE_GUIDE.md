# Code Structure Guide

## Folder Organization

Your code is now organized into three main functional areas:

```
src/
├── common/              (Shared utilities)
│   ├── OperationCounter.java
│   └── DataGenerator.java
│
├── simple/              (Simple Multiplication)
│   └── SimpleMultiplication.java
│
├── karatsuba/           (Karatsuba Algorithm)
│   └── Karatsuba.java
│
└── comparison/          (Comparison & Analysis)
    ├── ComparisonAnalysis.java
    ├── AnalysisReport.java
    ├── GraphComparison.java
    ├── Karatsuba_graph.java
    └── LargeNumberComparison.java
```

## Running the Programs

### From `src/` directory:

**1. Simple Multiplication Analysis (1-100 digits)**
```bash
java -cp . simple.SimpleMultiplication
```
Output: Displays operation count table for 1-100 digit numbers

**2. Karatsuba Analysis (1-100 digits)**
```bash
java -cp . karatsuba.Karatsuba
```
Output: Displays operation count table for 1-100 digit numbers

**3. Comparison Analysis (1-100 digits)**
```bash
java -cp . comparison.ComparisonAnalysis
```
Output: 
- Generates `comparison_results.csv`
- Generates `comparison_graph.png`
- Displays comparison table

**4. Large Number Comparison (10-1000 digits)**
```bash
java -cp . comparison.LargeNumberComparison
```
Output:
- Generates `large_comparison_results.csv`
- Generates `large_comparison_graph.png`

**5. Karatsuba Graph (1-100 digits)**
```bash
java -cp . comparison.Karatsuba_graph
```
Output:
- Generates `karatsuba_plot.png`
- Generates `karatsuba_data.csv`

**6. Generate Analysis Report**
```bash
java -cp . comparison.AnalysisReport
```
Output: Generates `ALGORITHM_ANALYSIS_REPORT.md`

## Package Structure

- **`common`** - Shared utility classes
  - `OperationCounter`: Tracks 8 types of primitive operations
  - `DataGenerator`: Generates random n-digit numbers

- **`simple`** - Simple Multiplication (O(n²))
  - `SimpleMultiplication`: Grade-school multiplication algorithm

- **`karatsuba`** - Karatsuba Algorithm (O(n¹·⁵⁸⁵))
  - `Karatsuba`: Divide-and-conquer multiplication

- **`comparison`** - Analysis and Visualization
  - `ComparisonAnalysis`: Runs both algorithms on 1-100 digits
  - `LargeNumberComparison`: Tests extreme range (10-1000 digits)
  - `GraphComparison`: Generates dual-algorithm visualization
  - `Karatsuba_graph`: Single algorithm visualization
  - `AnalysisReport`: Markdown report generation

## Compilation

Recompile all packages:
```bash
cd src
javac common/*.java simple/*.java karatsuba/*.java comparison/*.java
```

## Output Files

- `comparison_results.csv` - Operation counts (1-100 digits)
- `comparison_graph.png` - Dual algorithm comparison graph
- `large_comparison_results.csv` - Operation counts (10-1000 digits)
- `large_comparison_graph.png` - Large number comparison graph
- `karatsuba_plot.png` - Karatsuba single algorithm graph
- `karatsuba_data.csv` - Karatsuba operation data
- `ALGORITHM_ANALYSIS_REPORT.md` - Comprehensive analysis report
