/**
 * OperationCounter: Utility class to track primitive operations
 * Counts the following primitive operations:
 * - Assignments
 * - Arithmetic operations (addition, subtraction, multiplication, division, modulo)
 * - Comparisons
 * - Array accesses
 */
package common;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OperationCounter {
    private long assignments;
    private long additions;
    private long subtractions;
    private long multiplications;
    private long divisions;
    private long modulos;
    private long comparisons;
    private long arrayAccesses;

    public OperationCounter() {
        reset();
    }

    public void reset() {
        assignments = 0;
        additions = 0;
        subtractions = 0;
        multiplications = 0;
        divisions = 0;
        modulos = 0;
        comparisons = 0;
        arrayAccesses = 0;
    }

    // Record operations
    public void recordAssignment(long count) { assignments += count; }
    public void recordAddition(long count) { additions += count; }
    public void recordSubtraction(long count) { subtractions += count; }
    public void recordMultiplication(long count) { multiplications += count; }
    public void recordDivision(long count) { divisions += count; }
    public void recordModulo(long count) { modulos += count; }
    public void recordComparison(long count) { comparisons += count; }
    public void recordArrayAccess(long count) { arrayAccesses += count; }

    // Get totals
    public long getAssignments() { return assignments; }
    public long getAdditions() { return additions; }
    public long getSubtractions() { return subtractions; }
    public long getMultiplications() { return multiplications; }
    public long getDivisions() { return divisions; }
    public long getModulos() { return modulos; }
    public long getComparisons() { return comparisons; }
    public long getArrayAccesses() { return arrayAccesses; }

    /**
     * Total number of primitive operations
     */
    public long getTotalOperations() {
        return assignments + additions + subtractions + multiplications 
               + divisions + modulos + comparisons + arrayAccesses;
    }

    @Override
    public String toString() {
        return String.format(
            "Operations Count:\n" +
            "  Assignments:      %d\n" +
            "  Additions:        %d\n" +
            "  Subtractions:     %d\n" +
            "  Multiplications:  %d\n" +
            "  Divisions:        %d\n" +
            "  Modulos:          %d\n" +
            "  Comparisons:      %d\n" +
            "  Array Accesses:   %d\n" +
            "  TOTAL:            %d",
            assignments, additions, subtractions, multiplications,
            divisions, modulos, comparisons, arrayAccesses,
            getTotalOperations()
        );
    }

    public String getDetailedCSV() {
        return String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d",
            assignments, additions, subtractions, multiplications,
            divisions, modulos, comparisons, arrayAccesses, getTotalOperations());
    }

    public static String getCSVHeader() {
        return "Assignments,Additions,Subtractions,Multiplications,Divisions,Modulos,Comparisons,ArrayAccesses,Total";
    }
}
