import java.util.*;

public class RecursionProblems {

    public static void main(String[] args) {

         /* All questions are from chapter 8 of 'Cracking the coding interview' */
        //tripleStep();

        //robotInGrid();

        //magicIndex();

        //allSubsets();

        //towersOfHanoi();

        //stringPermutationsNoDups();

        //stringPermutationsWithDups();

        //parenthasis();

        //coins();

        eightQueens();
    }

    //8.1
    // A child is running up a staircase with n steps and can hop either 1 step, 2 steps, or 3 steps at a time.
    // Implement a method to count how many possible ways the child can run up the stairs.
    public static void tripleStep(){

        //count how many ways are there to get to N in jumps of 1, 2, or 3 at a time
        int n = 100;

        if (n <= 0)
            System.out.println("invalid input");

        boolean simple_recursion = false;
        boolean memoization = true;

        if (simple_recursion)
            System.out.println(tripleStep(n));
        else if (memoization){
            int[] memo = new int[n + 1];
            System.out.println((tripleStepMemo(n, memo)));
        }
    }

    private static int tripleStep(int n) {

        //base cases (you can have n==1 as the first base case, but because it's tricky we'll hand compute the value for n==3
        if (n == 1)
            return 1;
        if (n == 2)
            return 2;
        if (n == 3)
            return 4;

        return tripleStep(n - 1) + tripleStep(n - 2) + tripleStep(n - 3);

    }

    private static int tripleStepMemo(int n, int[] memo) {

        //base cases
        if (n == 1)
            return 1;
        if (n == 2)
            return 2;
        if (n == 3)
            return 4;

        if (memo[n] != 0)
            return memo[n];
        else
            return memo[n] = tripleStepMemo(n - 1, memo) + tripleStepMemo(n - 2, memo) + tripleStepMemo(n - 3, memo);
    }


    //8.2
    //Imagine a robot sitting on the upper left corner of grid with r rows and c columns.
    //The robot can only move in two directions, right and down, but certain cells are "off limits" such that the robot cannot step on them.
    //Design an algorithm to find a path for the robot from the top left to the bottom right

    //I did more, and I print all ways
    private static void robotInGrid() {

        //let's mark an 'off limit' cell with 0
        int n = 4, m = 5;
        int[][]grid = { {1,0,1,1,1},
                        {1,1,0,1,1},
                        {1,1,1,0,1},
                        {1,0,1,1,1}};

        Stack<String> directions = new Stack<>(); //we'll use a stack to store the path
        boolean[][] failed_points = new boolean[n][m];  //actually, a hashset is more fitting, but that requires working with a class pair
        if (findAndPrintPath(grid, n - 1, m - 1, failed_points, directions) == false)
            System.out.println("could not find a path!");
    }

    private static boolean findAndPrintPath(int[][] grid, int r, int c, boolean[][] failed_points, Stack<String> directions) {

        //base case 1: out of bounds
        if (r < 0 || c < 0)
            return false;

        //base case 2: reached destination. Print the stack (but preserve it)
        if (r == 0 && c == 0){
            Stack<String> aux_stack = new Stack<>();
            while (!directions.isEmpty()) {
                System.out.print(directions.peek());
                aux_stack.push(directions.pop());
            }
            while (!aux_stack.isEmpty())
                directions.push(aux_stack.pop());
            System.out.println();
            return true;
        }

        //cannot pass through this cell
        if (grid[r][c] == 0)
            return false;

        //already concluded that this is a failed point
        if (failed_points[r][c] == true)
            return false;

        boolean found = false;
        //try to go up
        directions.push(" Down ");
        if (findAndPrintPath(grid, r - 1, c, failed_points, directions) == true)
            found = true;
        directions.pop();

        //try to go left
        directions.push(" Right ");
        if (findAndPrintPath(grid, r, c - 1, failed_points, directions) == true)
            found = true;
        directions.pop();

        if (!found)
            failed_points[r][c] = true;

        return found;
    }

    //8.3
    //A magic index in an array[0.. n-1] is defined to be an index such that arr[i] = i.
    //Given a sorted array of distinct integers, write a method to find a magic index, if one exists
    public static void magicIndex() {

        int[] arr = {-7, -5, -3, -2, 0, 4, 5, 6, 12};
        int magic_index = magicIndex(arr, 0, arr.length - 1);
        System.out.println((magic_index == -1)? "no magic index" : magic_index);
    }

    private static int magicIndex(int[] arr, int low, int high) {

        //base cases
        if (low > high)
            return -1;

        int middle_index = (low + high) / 2;
        if (arr[middle_index] == middle_index)
            return middle_index;
        else if (arr[middle_index] > middle_index)
            return magicIndex(arr, low, middle_index - 1);
        else
            return magicIndex(arr, middle_index + 1, high);
    }


    //8.4
    //print all subsets of a set
    public static void allSubsets() {

        int[] set = {1,2,3,4,5}; //let's represent the set using an array, it will be simpler

        boolean recursive_solution = false;
        boolean wise_ass_solution = true;

        List<HashSet<Integer>> all_subsets = new ArrayList<>();

        if (recursive_solution) {
            allSubsets(set, all_subsets, set.length - 1);
        }
        else if (wise_ass_solution){
            //every number from 0 to 2^size - 1 represents in its binary representation which elements should and should not be taken
            for (int n = 0; n < Math.pow(2, set.length); n++){
                HashSet<Integer> subset = convertIntToSubset(set, n); //don't forget to modularize!
                all_subsets.add(subset);
            }
        }
        //print all subsets
        for (HashSet<Integer> subset : all_subsets) {
            System.out.print("{");
            for (int subset_member : subset)
                System.out.print(subset_member + " ");
            System.out.println("}");
        }
    }

    //find all subsets from the set using only indices [0....index]
    private static void allSubsets(int[] set, List<HashSet<Integer>> all_subsets, int index) {

        //base case
        if (index < 0) {
            HashSet<Integer> empty_set = new HashSet<>();
            all_subsets.add(empty_set);
            return;
        }

        //create all subsets up to this number, and for each create a version with the new item
        allSubsets(set, all_subsets, index - 1);
        ListIterator<HashSet<Integer>> iter = all_subsets.listIterator();
        while (iter.hasNext()){
            HashSet<Integer>subset = iter.next();
            HashSet<Integer> subset_with_new_index = (HashSet<Integer>)subset.clone();
            subset_with_new_index.add(set[index]);
            iter.add(subset_with_new_index);
        }
    }


    private static HashSet<Integer> convertIntToSubset(int[] set, int n) {
        HashSet<Integer> subset = new HashSet<>();
        for (int i = 0; n > 0; n/=2, i++)
            if ((n % 2) == 1)
                subset.add(set[i]);
        return subset;
    }

    //8.6 - solve using only stacks
    public static void towersOfHanoi() {

        int n = 10;

        Stack<Integer> source = new Stack<>();
        Stack<Integer> target = new Stack<>();
        Stack<Integer> aux = new Stack<>();

        //starting position is with all disks on the left
        for (int disk_size = n; disk_size >= 1; disk_size--)
            source.push(disk_size);

        System.out.println(towersOfHanoi(n, source, target, aux));
    }

    private static int towersOfHanoi(int n, Stack<Integer> source, Stack<Integer> target, Stack<Integer> aux) {

        //base case - done. be careful not to use the size of source as the base case
        if (n == 0)
            return 0;

        //base case - only one disk to move
        if (n == 1) {
            target.push(source.pop());
            return 1;
        }

        int first_step = towersOfHanoi(n - 1, source, aux, target);
        target.push(source.pop());
        int second_step = towersOfHanoi(n - 1, aux, target, source);
        return first_step + 1 + second_step;
    }

    //8.7
    public static void stringPermutationsNoDups() {

        String input = "abcdZ";
        ArrayList<String> permutations = stringPermutationsNoDups(input);
        System.out.println(permutations.size() + " permutations:");
        for (String permutation: permutations)
            System.out.println(permutation);
    }

    private static  ArrayList<String> stringPermutationsNoDups(String input) {

        ArrayList<String> permutations = new ArrayList<String>();
        if (input.length() == 0) {
            permutations.add("");
            return permutations;
        }

        char first_char = input.charAt(0);
        String remainder = input.substring(1);
        ArrayList<String> prev_permutations = stringPermutationsNoDups(remainder);

        //put the first char in every possible location
        for (String existing_permutation : prev_permutations)
            for (int new_char_location = 0; new_char_location <= existing_permutation.length(); new_char_location++)
                permutations.add(insertCharAtLocation(existing_permutation, first_char, new_char_location));
        return permutations;
    }

    //very elegant to take this into a separate function!
    //note that substring(0,0) gives an empty string, and substring (string.length) as well
    private static String insertCharAtLocation(String string, char char_to_insert, int new_char_location) {
        return string.substring(0, new_char_location) + char_to_insert + string.substring(new_char_location);
    }

    //8.8 - same question as before, but the string might contain duplicate characters, but our permutations must by unique
    public static void stringPermutationsWithDups() {

        String input = "aaaaaaaab";

        //start-off by counting how many time each letter appear
        HashMap<Character, Integer> letter_frequency_map = countLetterFrequency(input);

        ArrayList<String> permutations = stringPermutationsWithDups(letter_frequency_map);

        //print results
        System.out.println(permutations.size() + " permutations:");
        for (String permutation: permutations)
            System.out.println(permutation);
    }

    private static ArrayList<String> stringPermutationsWithDups(HashMap<Character, Integer> letter_frequency_map) {

        ArrayList<String> permutations = new ArrayList<>();

        //base case - empty map, all frequencies are zero
        if (allValuesZero(letter_frequency_map)){
            permutations.add("");
            return  permutations;
        }

        //for each letter, fix it as the first character and append to it all the permutations of the rest of the characters
        for (Character letter : letter_frequency_map.keySet()){
            int frequency = letter_frequency_map.get(letter);

            if (frequency > 0) {

                //decrease the count for this letter and recurse, building the rest of the permutations
                changeCount(letter_frequency_map, letter, -1);
                ArrayList<String> prev_permutations = stringPermutationsWithDups(letter_frequency_map);

                //append all permutations to the letter we fixed as the first one
                for (String permutation : prev_permutations) {
                    permutation = letter + permutation;
                    permutations.add(permutation);
                }

                //'backtrack' by re-adding the letter we removed
                changeCount(letter_frequency_map, letter, 1);
            }
        }

        return  permutations;
    }

    private static HashMap<Character,Integer> countLetterFrequency(String input) {
        HashMap<Character,Integer> letter_frequency_map = new HashMap<>();
        for (Character letter: input.toCharArray()){
            if (!letter_frequency_map.containsKey(letter))
                letter_frequency_map.put(letter, 1);
            else
                changeCount(letter_frequency_map, letter, 1);
        }
        return  letter_frequency_map;
    }


    private static boolean allValuesZero(HashMap<Character, Integer> letter_frequency_map) {

        for (int val : letter_frequency_map.values())
            if (val != 0)
                return false;
        return true;
    }


    private static void changeCount(HashMap<Character, Integer> letter_frequency_map, Character letter, int modification) {
        letter_frequency_map.put(letter, letter_frequency_map.get(letter) + modification);
    }


    //8.8
    //print all valid (e.g., properly opened and closed) combinations of n pairs of parentheses.
    public static void parenthasis() {

        int n = 3;
        ArrayList<String> expressions = new ArrayList<>();
        parenthasis(n, "", expressions, n, n);
        for (String expression : expressions)
            System.out.print(expression + " , ");
    }

    private static void parenthasis(int n, String expression, ArrayList<String> expressions, int left_remaining, int right_remaining) {

        //base case 1 - done
        if (left_remaining == 0 && right_remaining == 0) {
            expressions.add(expression);
            return;
        }

        //base case 2 - illegal.
        //The only delicate observation is that the expression is legal iff at any given point there are more open than close (or at
        //least equal) which means that there are more remaining close than open (or at least equal)
        if (left_remaining < 0 || left_remaining > right_remaining)
            return;

        //add left and recurse, add right and recurse
        parenthasis(n, expression + '(', expressions, left_remaining - 1, right_remaining);
        parenthasis(n, expression + ')', expressions, left_remaining, right_remaining - 1);
    }


    //8.10
    //Given an infinite number of quarters (25 cents), dimes (10 cents), nickels (5 cents),
    //and pennies (1 cent), write code to calculate the number of ways of representing n cents
    public static void coins() {

        int n = 5000; //does not return in basic recursive style

        int[] coins = {1,5,10,25};

        boolean basic_recursive = false;
        boolean memoization = true;
        boolean dynamic_programming = false;

        if (basic_recursive)
            System.out.println(coins(n, coins, coins.length - 1));
        else if (memoization) {
            Map<String, Integer> memo = new HashMap<>();
            System.out.println(coinsMemo(n, coins, coins.length - 1, memo));
        }
        else if (dynamic_programming){
            int[][] computed_values = new int[n + 1][coins.length + 1];
            for (int col = 0; col <= coins.length; col++)
                computed_values[0][col] = 1;
            for (int row = 0; row <= n; row++)
                computed_values[row][0] = 0;
            System.out.println(coinsDP(n, coins.length, coins, computed_values));
        }

    }

    //how many ways are there to create a sum of n using only coins from coins[0...biggest_coin_index]
    private static int coins(int n, int[] coins, int biggest_coin_index) {

        //base cases
        if (n == 0)
            return 1;

        if (biggest_coin_index < 0)
            return 0;

        if (n < 0)
            return 0;

        //either use the highest coin or not
        return coins(n, coins, biggest_coin_index - 1) + coins(n - coins[biggest_coin_index], coins, biggest_coin_index);
    }


    private static int coinsMemo(int n, int[] coins, int biggest_coin_index, Map<String, Integer> memo) {

        //base cases
        if (n == 0)
            return 1;

        if (biggest_coin_index < 0)
            return 0;

        if (n < 0)
            return 0;

        String key = n + "_" + biggest_coin_index;
        if (memo.containsKey(key))
            return memo.get(key);
        else {
            int value = coinsMemo(n, coins, biggest_coin_index - 1, memo) + coinsMemo(n - coins[biggest_coin_index], coins, biggest_coin_index, memo);
            memo.put(key, value);
            return value;
        }
    }

    private static int coinsDP(int n, int num_coins, int[] coins, int[][] computed_values) {

        for (int row = 1; row <= n; row++){
            for (int col = 1; col <= num_coins; col++){
                int val = computed_values[row][col - 1];
                if (row - coins[col - 1] >= 0)
                    val += computed_values[row - coins[col - 1]][col];
                computed_values[row][col] = val;
            }
        }
        return computed_values[n][num_coins];
    }

    //8.12
    //print all ways of arranging eight queens on an 8x8 chess board
    public static void eightQueens() {

        int size = 8;

        ArrayList<int[]> solutions = new ArrayList<>();

        //col_placements[i] denotes the column number in [0..7] where we place z queen on row i in [0...7]
        int[] col_placements = new int[size];

        eightQueens(size, col_placements, 0, solutions); //start with first row

        //print all solutions
        System.out.println("There are " + solutions.size() + " solutions:");
        for (int[] solution : solutions) {
            for (int col : solution) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }

    //find all possible placements, starting at row 'row_num', taking into account previous placements
    private static void eightQueens(int size, int[] col_placements, int row_num, ArrayList<int[]> solutions) {

        //base case - found a solution
        if (row_num == size) {
            solutions.add(col_placements.clone());
            return;
        }

        //for every valid placement, place and recurse
        for (int col = 0; col < size; col++){
            if (validPlacement(row_num, col, col_placements)) {
                col_placements[row_num] = col;
                eightQueens(size, col_placements, row_num + 1, solutions);
            }
        }
    }

    private static boolean validPlacement(int row_num, int col, int[] prev_col_placements) {
        for (int row = 0; row < row_num; row++){
            if (prev_col_placements[row] == col)
                return false;
            if (Math.abs(row_num - row) == Math.abs(col - prev_col_placements[row]))
                return false;
        }
        return true;
    }
}

