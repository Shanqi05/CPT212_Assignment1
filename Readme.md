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

### Step 3: Run Large Number Comparison (2-10000 digits)
```
java -cp . comparison.LargeNumberComparison
```

---

## Output Files
- simple_multiplication_graph.png
- karatsuba_graph.png
- large_comparison_graph.png
- large_comparison_results.csv