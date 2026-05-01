# CPT212 Part 2: Algorithm Comparison

## How to View Graphs

Run the program from the `src/` directory:
```
java -cp . comparison.LargeNumberComparison
```

The graph will be generated in the `src/` directory:
   - `large_comparison_graph.png`

Open the `.png` file to view the graph showing comparison of Simple Multiplication vs Karatsuba for large numbers (2 to 10,000 digits).

## How to Run

### Step 1: Navigate to src
```
cd src
```

### Step 2: Compile
```
javac common/*.java simple/*.java karatsuba/*.java comparison/*.java
```

### Step 3: Run All Three Programs

**Program 1: Simple Multiplication Algorithm**
```
java -cp . simple.SimpleMultiplication
```
Output: Terminal display of operations count + `simple_multiplication_graph.png`

**Program 2: Karatsuba Algorithm**
```
java -cp . karatsuba.Karatsuba
```
Output: Terminal display of operations count + `karatsuba_graph.png`

**Program 3: Large Number Comparison (2-10000 digits)**
```
java -cp . comparison.LargeNumberComparison
```
Output: Terminal display comparing both algorithms + `large_comparison_graph.png` + `large_comparison_results.csv`

---

## Output Files Generated
All output files are saved in the `src/` directory:
- `simple_multiplication_graph.png` - Graph of Simple Multiplication performance
- `karatsuba_graph.png` - Graph of Karatsuba performance
- `large_comparison_graph.png` - Comparison graph of both algorithms
- `large_comparison_results.csv` - Data table with operation counts