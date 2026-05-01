# CPT212 Part 2: Algorithm Comparison

## How to Run

### Step 1: Navigate to src
\\\ash
cd src
\\\

### Step 2: Compile
\\\ash
javac common/*.java simple/*.java karatsuba/*.java comparison/*.java
\\\

### Step 3: Run Programs

**Simple Multiplication**
\\\ash
java -cp . simple.SimpleMultiplication
\\\

**Karatsuba Algorithm**
\\\ash
java -cp . karatsuba.Karatsuba
\\\

**Small Numbers Comparison (1-100 digits)**
\\\ash
java -cp . comparison.GraphComparison
\\\

**Karatsuba Only**
\\\ash
java -cp . comparison.Karatsuba_graph
\\\

**Large Numbers Comparison (10-1000 digits)**
\\\ash
java -cp . comparison.LargeNumberComparison
\\\

**Full Analysis**
\\\ash
java -cp . comparison.ComparisonAnalysis
\\\

**Generate Report**
\\\ash
java -cp . comparison.AnalysisReport
\\\

---

## Output Files
- comparison_graph.png
- karatsuba_plot.png
- large_comparison_graph.png
- comparison_results.csv
- karatsuba_data.csv
- large_comparison_results.csv
- ALGORITHM_ANALYSIS_REPORT.md
