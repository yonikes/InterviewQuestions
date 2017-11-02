import java.util.*;

public class InterviewQuestions {

    private static final int SUDOKU_BOARD_SIZE = 9;
    private static final int SUDOKU_BOX_SIZE = 3;

    public static void main(String [] args){

        /* These questions from real interviews, rumors of real interviews, or simply things that might come up */

        oddNumbers();

        countTheElements();

        getMinimumDifference();

        MultiplyTwoNumbers();

        StringsArrayPermutation();

        AllSubsetsOfSizeK();

        IsSodukuValid();

        QuickSort();

        FindKthSmallestEllement();

        areBracketsValid();

        streamIntoMatrix();

        oceansRiversAndMountains();

        numberOfPathsInMatrix();

        addIntegresByArray();

        minimumCostPathInMatrix();

        histogram();

        characterFrequency();

        serializeTree();

        optimalCoverage();

        stickersToSpellWord();

    }

    //asked at hackerRank in preparation for Amazon interview
    public static void oddNumbers() {

        //return an array with all the odd numbers in the range [l...r]
        int l = 4;
        int r = 7;

        int[] odd_numbers = oddNumbers(l, r);
        if (odd_numbers != null)
            for (int val : odd_numbers)
                System.out.print(val + " ");

    }

    private static int[] oddNumbers(int l, int r) {

        //edge case that needs addressing
        if (l == r && l % 2 == 0)
            return null;

        //determine the number of odd elements (we can just add them to a dynamic array, but that's more space efficient)
        int size = ((r - l) / 2);
        if (l % 2 == 1 || r % 2 == 1)
            size++;
        int[] odd_numbers = new int[size];

        //find the first odd number
        int odd_number;
        if (l % 2 == 1)
            odd_number = l;
        else
            odd_number = l + 1;

        //add all odd elements to the array
        for (int i = 0; i < size; i++, odd_number+=2)
            odd_numbers[i] = odd_number;

        return odd_numbers;
    }

    //asked at Amazon online interview
    public static void countTheElements() {

        //given two arrays 'nums' and 'maxes', return an array 'ans' such that ans[i] contains how many numbers in 'nums' are smaller than or equal to maxes[i]
        int[] ans;
        int[] nums = {2, 9, 3, 3, 3};
        int[] maxes = {3, 1, 2};

        ans = countTheElements(nums, maxes);
        for (int elem : ans)
            System.out.print(elem + " ");;
    }

    private static int[] countTheElements(int[] nums, int[] maxes) {

        if (maxes == null || nums == null || maxes.length == 0 || nums.length == 0)
            return null;

        //an obvious brute force solution will be to go over all elements in maxes, and for each one scan all elements in nums this is O(n*m);

        //note that we cannot sort maxes, because we lose the order of our initial elements in maxes

        //if the elements themselves were bounded by a small number, we could have used hash map from a value to its number of appearances (that's what we did in the same question in GeeksforGeeks)

        //Best - after sorting nums, we can do a variant on binary search for each element in maxes! that's NlogN + MlogN = (M+N)LogN

        Arrays.sort(nums);

        int[] ans = new int[maxes.length];
        for (int i = 0; i < maxes.length; i++) {

            //using Arrays built-in binary search is good but tricky, because the elements are not necessarily in the sorted array...
            int position_in_nums = Arrays.binarySearch(nums, maxes[i]);

            //documentation of binary search return value: index of the search key, if it is contained in the array; otherwise, (-(insertion point) - 1).
            //it is done so you'll have a clear indicator if the element is present or not. If it's not, than what we get is the 2's complement of the 'insertion' point, which is what iwe want.
            if (position_in_nums < 0)
                position_in_nums = ~position_in_nums;

            //Another 'gotcha' is that if the element exists, we want to count it as well.
            //Also, what if exists several times? according to the documentation, "If the array contains multiple elements with the specified value, there is no guarantee which one will be found"
            //the following loop solves both issues
            else while ((position_in_nums < nums.length) && (nums[position_in_nums] == maxes[i]))
                    position_in_nums++;
            ans[i] = position_in_nums;
        }

        return ans;
    }

    //asked at amazon online test
    public static void getMinimumDifference() {

        //given two strings, get the minimum number of letter to change (on either one) so they will become anagrams
        String[] a = {"ubu", "abd"};
        String[] b = {"lzt", "gbe"};

        int[] ans = getMinimumDifference(a, b);
        for (int elem : ans)
            System.out.println(elem);
    }

    private static int[] getMinimumDifference(String[] a, String[] b) {

        int[] ans = new int[a.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = getMinimumDifference(a[i], b[i]);
        return ans;
    }

    private static int getMinimumDifference(String a, String b) {

        //if the strings are not in the same length, they cannot be anagrams
        if (a.length() != b.length())
            return -1;

        /* the algorithm is simple: count (using a hashmap) how many times each letter appears in both strings.
           Then scan one of the maps, comparing the count for each letter: for each letter in 'a', if it appears n times in 'a' but only m  times in 'b' (m < n), add the difference between them to our count */

        //create both hashmaps
        HashMap<Character, Integer> a_frequencies = createFrequencyMap(a);
        HashMap<Character, Integer> b_frequencies = createFrequencyMap(b);

        //scan them
        int ans = 0;
        for (char c : a_frequencies.keySet()){
            int a_frequency = a_frequencies.get(c);
            int b_frequency = b_frequencies.getOrDefault(c, 0); //remember that 'c' might not appear in 'b', important to use a default value

            //it's important to check only if the element is bigger (as opposed to simply not equal), to avoid counting each difference twice.
            if (a_frequency > b_frequency)
                ans += (a_frequency - b_frequency);
        }
        return ans;
    }

    private static HashMap<Character,Integer> createFrequencyMap(String s) {

        HashMap<Character, Integer> frequencies = new HashMap<>();

        for(char c : s.toCharArray()){
            if (!frequencies.containsKey(c)) //this cak also be made simpler by using getOrDefault
                frequencies.put(c, 1);
            else
                frequencies.put(c, frequencies.get(c) + 1);
        }

        return frequencies;
    }

    //A question asked at MicroSemi interview
    public static void MultiplyTwoNumbers() {
        int input_a = Integer.MAX_VALUE;
        int input_b = 2;

        //the goal of this code is to multiply the numbers without using multiplication
        /*
        int ans = 0;
        while (input_b != 0){
            if (input_b % 2 == 1)
                ans += input_a;
            input_b = input_b >> 1;
            input_a = input_a << 1;
        }
        */

        //and the goal of this code is to detect overflow when multiplying two ints
        /*
        long safe_result = (long)input_a * input_b; //by casting, we ensure there will not be overflow
        if (safe_result > Integer.MAX_VALUE)
            System.out.println("Overflow detected!");
        else {
            int ans = (int)safe_result & 0xffffffff;
            System.out.println(ans);
        }
        */
    }

    //a question Gil was asked at Google.Given an array of String arrays, print all possible permutations
    public static void StringsArrayPermutation() {

        String[][] input = {{"big", "small", "fat"}, {"blue", "red", "orange", "black"}, {"kid", "zebra"}};

        //this naive implementation will work if the number of arrays is known in advance (like 3 in this example)
        /*
        for (int i = 0; i < input[0].length; i++){
            for (int j = 0; j < input[1].length; j++){
                for (int k = 0; k < input[2].length; k++){
                    System.out.println(input[0][i] + " " + input[1][j] + " " + input[2][k]);
                }
            }
        }
        */

        //initialize an aux array to hold the current location of all arrays
        int[] array_pointers = new int[input.length];
        Arrays.fill(array_pointers, 0);

        //we assume the first permutation exists
        for (int i = 0; i < input.length; i++)
            System.out.print(input[i][array_pointers[i]] + " ");
        System.out.println();

        //iterate through all permutations
        while(HasNext(input, array_pointers)){
            Next(input, array_pointers);
            for (int i = 0; i < input.length; i++)
                System.out.print(input[i][array_pointers[i]] + " ");
            System.out.println();
        }
    }

    //assumption: HasNext() had returned true
    private static void Next(String[][] input, int[] array_pointers) {
        //find the first pointer location which can be incremented, resetting positions at full capacities as we go
        int slot_to_increment = array_pointers.length - 1;
        while (array_pointers[slot_to_increment] == input[slot_to_increment].length - 1){
            array_pointers[slot_to_increment] = 0;
            slot_to_increment--;
        }
        array_pointers[slot_to_increment]++;
    }

    private static boolean HasNext(String[][] input, int[] array_pointers) {
        boolean ans = false;
        for (int i = 0; i < array_pointers.length; i++){
            if (array_pointers[i] < input[i].length - 1){
                ans = true;
                break;
            }
        }
        return ans;
    }

    /* A good question to ask at an interview, and a subroutine in my implementation for held-karp algorythm to solve TSP  */
    public static void AllSubsetsOfSizeK() {

        int[]set = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int n = set.length;
        int k = 4; //we want all subsets of this size
        ArrayList<int[]> all_subsets = new ArrayList<>();

        AllSubsetsOfSizeKAux(set, n, k, new int[k], 0, 0, all_subsets);
        System.out.println("There are " + all_subsets.size() + " subsets:");
        for (int[] subset : all_subsets){
            System.out.print("{ ");
            for (int i : subset)
                System.out.print(i + " ");
            System.out.println("}");
        }
    }

    private static void AllSubsetsOfSizeKAux(int[] set, int n, int k, int[] subset, int num_elements_in_subset,
                                             int set_index, ArrayList<int[]> all_subsets) {
        //base case 1: we have filled up our set
        if (num_elements_in_subset == k) {
            all_subsets.add(subset.clone()); //crucial to clone, otherwise the solution we added will keep changing after we add it to 'all_subsets'...
            return;
        }

        //base case 2: we have run out of elements to add to our set
        if (set_index == n)
            return;

        //Continue building a set that contains this element
        subset[num_elements_in_subset] = set[set_index];
        AllSubsetsOfSizeKAux(set, n, k, subset, num_elements_in_subset + 1 , set_index + 1, all_subsets);

        //Continue building a set that does not contain this element
        //note that here no explicit backtracking is needed, since that 'num_elements_in_subset', will guruntee we 'ignore' the assignment we made several lines ago
        AllSubsetsOfSizeKAux(set, n, k, subset, num_elements_in_subset, set_index + 1, all_subsets);
    }

    /* From geeks for geeks */
    public static void IsSodukuValid(){
        int[][]board =  {   {3, 0, 6, 5, 0, 8, 4, 0, 0},
                            {5, 2, 0, 0, 0, 0, 0, 0, 0},
                            {0, 8, 7, 0, 0, 0, 0, 3, 1},
                            {0, 0, 3, 0, 1, 0, 0, 8, 0},
                            {9, 0, 0, 8, 6, 3, 0, 0, 5},
                            {0, 5, 0, 0, 9, 0, 6, 0, 0},
                            {1, 3, 0, 0, 0, 0, 2, 5, 0},
                            {0, 0, 0, 0, 0, 0, 0, 7, 4},
                            {0, 0, 5, 2, 0, 6, 3, 0, 0}};

        System.out.println(IsSodukuValid(board));
    }

    public static boolean IsSodukuValid(int[][] board){

        //find the first undecided slot. If did not find any, return true (assumption: will not get a board with built in mistakes)
        boolean empty_slot_found = false;
        int empty_row = 0, empty_column = 0;
        for (int r = 0; (r < SUDOKU_BOARD_SIZE) && !empty_slot_found; r++){
            for (int c = 0; (c < SUDOKU_BOARD_SIZE) && !empty_slot_found; c++){
                if (board[r][c] == 0){
                    empty_slot_found = true;
                    empty_row = r;
                    empty_column = c;
                }
            }
        }
        if (!empty_slot_found)
            return true;

        //Check every possible value for the slot we found. If none fit, return false. For all valid values, recurse - if one of them succeeds, return true. If all fail, return false.
        for (int possible_value = 1; possible_value <= 9; possible_value++){
            if (ValidPlacement(board, empty_row, empty_column, possible_value)){
                board[empty_row][empty_column] = possible_value;
                if (IsSodukuValid(board))
                    return true;
                else
                    board[empty_row][empty_column] = 0; //backtrack
            }
        }
        return false;
    }


    private static boolean ValidPlacement(int[][]board, int r, int c, int possible_value) {

        //check row
        for (int row_check = 0; row_check < SUDOKU_BOARD_SIZE; row_check++)
            if (board[row_check][c] == possible_value)
                return false;

        //check column
        for (int column_check = 0; column_check < SUDOKU_BOARD_SIZE; column_check++)
            if (board[r][column_check] == possible_value)
                return false;

        //check conflicts within 3*3 box
        int box_first_row = r - (r % SUDOKU_BOX_SIZE);
        int box_first_column = c - (c % SUDOKU_BOX_SIZE);
        for (int box_row = box_first_row; box_row < box_first_row + SUDOKU_BOX_SIZE; box_row++) {
            for (int box_column = box_first_column; box_column < box_first_column + SUDOKU_BOX_SIZE; box_column++) {
                if (board[box_row][box_column] == possible_value)
                    return false;
            }
        }

        return true;
    }


    /* Good thing to know, and the 'partition' function is needed to solve 'FindKthSmallestEllement' */
    public static void QuickSort() {

        int[] arr = {4, 1, 8, -2, 1, 6, 55, 3, 0, 2};
        QuickSort(arr, 0, arr.length - 1);
        PrintArray(arr);
    }

    private static void QuickSort(int[] arr, int low, int high) {
        if (low < high){
            int pivot_index = partition(arr, low, high);
            QuickSort(arr, low, pivot_index - 1);
            QuickSort(arr, pivot_index + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {

        int originl_pivot_index = high; //in this version, we are always picking up the last element in the range as pivot
        int pivot_value = arr[originl_pivot_index];
        int pivot_correct_location = low; //initialization

        for (int i = low; i < high; i++){
            if (arr[i] < pivot_value){
                //when finding an element smaller than the pivot, make sure it's on the left side of the pivot by swapping it
                //with the current pivot location, and pushing the pivot one slot to the right.
                //looks a bit confusing for the first element if it's smaller than the pivot - indeed the swap will do nothing, but the increment is vital
                swap(arr, i, pivot_correct_location);
                pivot_correct_location++;
            }
        }
        //now final pivot index is in the correct place, put the pivot there
        swap(arr, pivot_correct_location, originl_pivot_index);

        return pivot_correct_location;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    private static void PrintArray(int[] arr) {
        for (int val : arr)
            System.out.print(val + " ");
        System.out.println();
    }


    /* A question with a nice solution, important (fr some reason called 'order statistics)  */
    public static void FindKthSmallestEllement() {

        //given an array of ints, find the k'th smallest element in O(n)
        int[]input = {5, 7, 1, 76, -2, 0, 4, 6, 2, 8, 44, 42, 39, 22, 9, 13};
        int k = 5;
        int []sorted_input = input.clone();
        Arrays.sort(sorted_input);
        PrintArray(sorted_input);

        if (k < 1 || k > input.length)
            System.out.println("Invalid K value");

        //the idea is to partition the array again and again like in quicksort - each time recursing only into the desired 'side' of the array
        System.out.print(FindKthSmallestEllement(input, k, 0, input.length - 1) + " ");

    }

    private static int FindKthSmallestEllement(int[] arr, int k, int low, int high) {

        if (low == high)
            return arr[low];

        //the array will divided into two parts
        int partition_index = partition(arr, low, high);

        //if we are lucky, we have partitions exactly where we want to
        if (partition_index + 1 == k)
            return arr[partition_index];

        //if the first K elements are in the left side of our partition, go left
        else if (partition_index + 1 > k)
            return FindKthSmallestEllement(arr, k, low, partition_index - 1);

        //else, go right. Note that 'k' should not be decremented, because we are working with array indices, not relative locations
        else
            return FindKthSmallestEllement(arr, k, partition_index + 1, high);
    }

    //a simple question allegedly asked at google
    public static void areBracketsValid(){

        //given a string with '(){}[]' determine if it's valid or not
        String input = "([][]({})[])";

        Stack<Character> stack = new Stack<>();
        for (char c : input.toCharArray()){
            if (opensStatement(c))
                stack.push(c);
            else {
                if (stack.empty()){
                    System.out.println("not valid");
                    return;
                }
                char top = stack.pop();
                if (!match(c, top)){
                    System.out.println("not valid");
                    return;
                }
            }
        }
        System.out.println((stack.empty())? "valid" : "not valid");
    }

    private static boolean opensStatement(char c) {
        return (c == '(' || c == '[' || c == '{');
    }

    private static boolean match(char close, char open) {
        return (((open == '(') && (close == ')')) ||
                ((open == '[') && (close == ']')) ||
                ((open == '{') && (close == '}')));

    }


    //Not so simple question allegedly asked at google
    public static void streamIntoMatrix(){

        //given an (infinite) stream of 0 and 1 which are filling up a matrix of size n*n, will there be a row or a column of all 0's or 1's
        String stream = "0101110101101011010001010010100101110101010010100101000000011111010101001010001010111110101000";
        int n = 1;

        char[] char_stream = stream.toCharArray();

        //keeping track of the size of the current row we are checking and its type
        char row_type = 0; //just to pass compilation..
        int row_size = 0;

        //for columns, we need to keep track of n columns simultaneously
        char[]column_type = new char[n];
        char[]column_size = new char[n];

        //it's funny because the stream is supposed to be endless, so this for loop should not really be here - it's replacing some kind of of scanning
        for (int stream_index = 0; stream_index < stream.length(); stream_index++){

            char new_char = char_stream[stream_index]; //Scanner sc = new Scanner(System.in); int t = sc.nextInt();

            //initialize the type if it's the first element
            if (stream_index == 0)
                row_type = new_char;

            //check if completes or continues row
            if (new_char == row_type) {
                row_size++;
                if (row_size == n) {
                    System.out.println("Found a row of " + new_char + " ending in index " + stream_index);
                    return;
                }
            } else {
                row_size = 1;
                row_type = new_char;
            }

            //initialize the type if it's the first element in any of the columns
            if (stream_index < n)
                column_type[stream_index] = new_char;

            //check if completes or continues column
            int relevant_column = stream_index % n;
            if (new_char == column_type[relevant_column]){
                column_size[relevant_column]++;
                if (column_size[relevant_column] == n) {
                    System.out.println("Found a column of " + new_char + " ending in index " + stream_index);
                    return;
                }
            }
            else{
                column_size[relevant_column] = 1;
                column_type[relevant_column] = new_char;
            }
        }
        System.out.println("did not find row or column with all 0's or 1's");
    }

    //complicated question allegedly asked at google
    public static void oceansRiversAndMountains() {

        //given a 0-1 matrix, 1 is mountain 0 is river, four edges are oceans, which flow through rivers. Find all matrix elements where water from all 4 oceans flow
        int[][]map = {  {0, 1, 0, 1, 1, 1},
                        {1, 1, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 1},
                        {0, 0, 1, 0, 1, 0},
                        {1, 1, 1, 0, 1, 0}};

        //scan the north frame, and when finding water, initiate a scan. Note that we only need to scan one edge of the frame
        for (int c = 0; c <  map[0].length; c++) {
            if (map[0][c] == 0) {
                ArrayList<Pair> pairs_in_scan = new ArrayList<>(); //needed to accumulate the vertices participating in the scan, to print them all at the end
                Set<DIRECTIONS> directions_found_on_scan = new HashSet<>(); //needed to check if this is a successful scan that reached all oceans
                DfsScan(map, new Pair(0, c), pairs_in_scan, directions_found_on_scan);
                if (directions_found_on_scan.size() == 4){
                    for (Pair pair : pairs_in_scan)
                        System.out.println(pair.toString() + " ");
                    return;
                }
            }
        }
        System.out.println("There are not rivers connecting all 4 oceans");
    }

    private static void DfsScan(int[][] map, Pair source, ArrayList<Pair> pairs_in_scan, Set<DIRECTIONS> directions_found_on_scan) {

        int r = source.r;
        int c = source.c;

        //base case (here is nicer than before recursing!)
        if ( r < 0  || c < 0 || r > map.length - 1 || c > map[0].length - 1)
            return;

        if (map[r][c] != 0)
            return;

        //this is instead marking as 'visited'. Useful both for this scan, and for the outer loop
        map[r][c] = -1;

        pairs_in_scan.add(source);

        //add to the direction set the relevant direction(s) if there are any
        if (r == 0)
            directions_found_on_scan.add(DIRECTIONS.NORTH);
        if (c == 0)
            directions_found_on_scan.add(DIRECTIONS.WEST);
        if (r == map.length - 1)
            directions_found_on_scan.add(DIRECTIONS.SOUTH);
        if (c == map[0].length - 1)
            directions_found_on_scan.add(DIRECTIONS.EAST);

        //call recursively to all 4 neighbours (water can't flow diagonally..)
        DfsScan(map, new Pair(r - 1, c), pairs_in_scan, directions_found_on_scan);
        DfsScan(map, new Pair(r, c - 1), pairs_in_scan, directions_found_on_scan);
        DfsScan(map, new Pair(r + 1, c), pairs_in_scan, directions_found_on_scan);
        DfsScan(map, new Pair(r, c + 1), pairs_in_scan, directions_found_on_scan);
    }

    public enum DIRECTIONS{
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    //presumably asked at google interview
    public static void numberOfPathsInMatrix() {

        int n = 50, m = 50;
        //calculate how many ways are there to get from [0,0] to [n-1][m-1] in a n*m matrix. Allowed to move right, down or diagonally

        boolean simple_recursion = true;
        boolean memoization = false;
        if (simple_recursion)
            System.out.println(numberOfPathsInMatrix(n - 1, m - 1));
        else if (memoization){
            int[][]memo = new int[n][m];
            System.out.println(numberOfPathsInMatrixMemo(n - 1, m - 1, memo));
        }
    }


    //how many ways are there to get from [0][0] to [row][col]
    private static int numberOfPathsInMatrix(int row, int col) {

        //base case 1
        if ((row == 0) && (col == 0))
            return 1;

        //base case 2
        if (row < 0 || col < 0)
            return 0;

        return numberOfPathsInMatrix(row - 1, col) + numberOfPathsInMatrix(row, col - 1) + numberOfPathsInMatrix(row - 1, col - 1);
    }


    private static int numberOfPathsInMatrixMemo(int row, int col, int[][]memo) {

        //base case 1
        if ((row == 0) && (col == 0))
            return 1;

        //base case 2
        if (row < 0 || col < 0)
            return 0;

        if (memo[row][col] != 0)
            return memo[row][col];
        return memo[row][col] = numberOfPathsInMatrixMemo(row - 1, col, memo) + numberOfPathsInMatrixMemo(row, col - 1, memo) + numberOfPathsInMatrixMemo(row - 1, col - 1, memo);
    }

    //presumably asked at google interview
    public static void addIntegresByArray() {

        //add 2 integers given as an array of digits
        int[] first = {3, 4, 2, 9};
        int[] second = {1,2};

        //we can convert both arrays to an int and do a normal addition.
        //Let's try to do it in place
        int first_index = first.length - 1, second_index = second.length - 1;
        int power = 0;
        int sum = 0;
        boolean carry = false;

        while (first_index >= 0 || second_index >= 0){
            int digit_sum = 0;
            if (first_index >= 0)
                digit_sum += first[first_index];
            if (second_index >= 0)
                digit_sum += second[second_index];
            if (carry)
                digit_sum++;
            sum += (digit_sum % 10) * Math.pow(10, power);
            power++;
            carry = (digit_sum >= 10)? true : false;
            first_index--;
            second_index--;
        }
        System.out.println(sum);
    }


    //A hard question from GeeksFor Geeks
    private static void minimumCostPathInMatrix() {

        //Given a square grid of size n, each cell of which contains integer cost which represents a cost to traverse through that cell, we need to find a path from top left cell to bottom right cell by which total cost incurred is minimum.
        //It is assumed that negative cost cycles do not exist in input matrix.
        int n = 2;
        int[][]costs = {{42, 93},
                        {7, 14}};


        /* we know that dijkstra's algoryhtm can find the cost from a source to all vertices in an undirected graph.
        if we look at the matrix as an undirected graph, with each vertix with (at most) 4 neighbours, we can apply it here */

        //initialize a DS to hold all distances
        int[][]distances = new int[n][n];
        for (int[] d : distances)
            Arrays.fill(d, Integer.MAX_VALUE);
        Pair source = new Pair(0,0);
        distances[source.r][source.c] = costs[source.r][source.c];

        //initialize a DS to mark all the vertices that were visited
        boolean[][]visited = new boolean[n][n];
        for (boolean[] b : visited)
            Arrays.fill(b, false);

        //stop if the destination was reached
        while (visited[n-1][n-1] == false){

            Pair new_source = ChooseSource(visited, distances, n);
            int source_distance = distances[new_source.r][new_source.c];

            /* create an array with the source's neighbours */
            Pair[]neighbours = new Pair[4];
            neighbours[0] = new Pair(new_source.r - 1, new_source.c); //go up
            neighbours[1] = new Pair(new_source.r + 1, new_source.c); //go down
            neighbours[2] = new Pair(new_source.r, new_source.c - 1); //go left
            neighbours[3] = new Pair(new_source.r, new_source.c + 1); //go left

            /* iterate over the unvisited neighbours of the source, relax their cost if possible */
            for (Pair neighbour : neighbours){
                if (neighbour.r < 0 || neighbour.c < 0 || neighbour.r > n - 1 || neighbour.c > n - 1)
                    continue;
                if (visited[neighbour.r][neighbour.c] == false){
                    int current_distance = distances[neighbour.r][neighbour.c];
                    int suggested_distance = source_distance + costs[neighbour.r][neighbour.c];
                    if (suggested_distance < current_distance)
                        distances[neighbour.r][neighbour.c] = suggested_distance;
                }
            }

            //mark the source as visited
            visited[new_source.r][new_source.c] = true;
        }

        System.out.println(distances[n-1][n-1]);
    }

    //return an unvisited vertix with the minimum distance. Precondition - there is at least one unvisited vertix
    private static Pair ChooseSource(boolean[][] visited, int[][] distances, int V){
        int min_row = -1, min_column = -1;
        int min_distance = Integer.MAX_VALUE;
        for (int r = 0; r < V; r++){
            for (int c = 0; c < V; c++){
                if ((visited[r][c] == false) && (distances[r][c] < min_distance)){
                    min_row = r;
                    min_column = c;
                    min_distance = distances[r][c];
                }
            }
        }
        return new Pair(min_row, min_column);
    }

    //A tricky questions from geeks for geeks:
    //Find the largest rectangular area possible in a given histogram where the largest rectangle can
    //be made of a number of contiguous bars

    //A solution from Geeks for Geeks. not fully understood, leaving it for now
    public static void histogram() {

        int[] hist = {6,2,5,4,5,1,6};
        System.out.println(histogram(hist));
    }

    private static int histogram(int[] hist) {

        if (hist.length == 0)
            return 0;

        //StackElement contains "start_pos" and "rectangle_height", and represents a possible solution
        Stack<StackElement> stack = new Stack<>();

        //start building first rectangle
        int start_pos = 0;
        int curr_hist_area = hist[0];
        int max_hist_area = curr_hist_area;

        for(int i = 1; i < hist.length; i++) {

            int prev_elem_height = hist[i - 1];
            int elem_height = hist[i];

            //new element is higher than what we are currently building. we will store what we are currently building in the stack, and start a new rectangle from this position
            if (prev_elem_height < elem_height) {

                // Store the current rectangle (the one that ends in the previous element)
                stack.push(new StackElement(start_pos, prev_elem_height));

                //start a new one from this position
                start_pos = i;
                curr_hist_area = elem_height;
            }
            //same height - continue building the current rectangle
            else if (prev_elem_height == elem_height) {
                curr_hist_area += elem_height;
            }
            //the previous element is higher than the current one
            else {

                //Current rectangle cannot span any further from this position (too high...) Check if the rectangle we built is the highest
                int curr_hist_width = (i - start_pos);
                int curr_hist_height = prev_elem_height;
                curr_hist_area = curr_hist_width * curr_hist_height;
                max_hist_area  = Math.max(max_hist_area, curr_hist_area);

                //Find if there is a stored rectangle, which also cannot span further right, which is the highest
                while ((!stack.empty()) && (stack.peek().height > elem_height /* this stored rectangle also cannot span any further right */)) {

                    // Take previously stored rectangle and Check if max area rectangle was found
                    StackElement rectangle = stack.pop();
                    prev_elem_height = rectangle.height;
                    start_pos = rectangle.start_position;
                    curr_hist_area = (i - start_pos) * prev_elem_height;
                    max_hist_area  = Math.max(max_hist_area, curr_hist_area);
                }

                //go on with building the current rectangle
                curr_hist_area = elem_height;
            }
        }

        //check if the last rectangle we built is actually the highest
        max_hist_area  = Math.max(max_hist_area, curr_hist_area);

        //the stack might still contain rectangles spanning from some position all the way to the end - check if one of them is the highest
        while (!stack.empty()) {

            // Take previously stored rectangle and Check if max area rectangle was found
            StackElement rectangle = stack.pop();
            int start_position = rectangle.start_position;
            int rectangle_height = rectangle.height;

            curr_hist_area = (hist.length - start_position) * rectangle_height;
            max_hist_area  = Math.max(max_hist_area, curr_hist_area);
        }

        return max_hist_area;

    }

    //asked at google mockup interview
    public static void characterFrequency() {

        //return the character with the most appearances in the string.
        String s = "";

        //this is not a complicated question, still couple of pitfalls
        System.out.print(characterFrequency(s));

    }

    private static char characterFrequency(String s) {

        int[] freq_count = new int[26];
        for (char c : s.toCharArray()){
            freq_count[c - 'a']++;
        }

        int max_appearances = -1;
        char ans = 0;
        for (int i = 0; i < freq_count.length; i++){
            if (freq_count[i] > 0 && freq_count[i] > max_appearances){ //first 'if' is important if we want correct result for an empty string
                max_appearances = freq_count[i];
                ans = (char) ('a' + i); //casting is a must!
            }
        }
        return ans;
    }


    //asked at facebook screening
    public static void serializeTree() {

        TreeNode root = new TreeNode(6);
        root.add(3);
        root.add(9);
        root.add(1);
        root.add(2);
        root.add(12);

        System.out.println(root.preOrder());
        String serialized = serializeTree(root);
        TreeNode deserialized = deserialize(serialized);
        System.out.println(deserialized.preOrder());
    }

    //output a string representation of the tree
    private static String serializeTree(TreeNode root) {

        String ans = "";

        if (root == null) {
            ans += "*";
            ans += "#";
        }
        else{
            ans += root.value;
            ans += "#";
            ans += serializeTree(root.left);
            ans += serializeTree(root.right);
        }

        return ans;
    }

    //given a string representation of a tree, construct the tree
    private static TreeNode deserialize(String serialized){
        Queue<String> queue = new LinkedList<>();
        queue.addAll(Arrays.asList(serialized.split("#")));
        return deserialize(queue);
    }

    private static TreeNode deserialize(Queue<String> queue) {

        String val = queue.remove();
        if (val.equals("*"))
            return null;
        else{
            TreeNode node = new TreeNode(Integer.valueOf(val));
            node.left = deserialize(queue);
            node.right = deserialize(queue);
            return node;
        }
    }


    //asked at google on-site interview
    public static void optimalCoverage() {

        //first input is an order set of points
        int[]points_to_cover = {1, 4, 12, 20};

        //second input is a set of segments
        ArrayList<Coverage> coverages = new ArrayList<>();
        coverages.add(new Coverage(1, 10, 100));
        coverages.add(new Coverage(-2, 100, 1000));
        coverages.add(new Coverage(8, 16, 20));
        coverages.add(new Coverage(6, 25, 10));

        //we'll use memoization to avoid duplicate computations
        int[] memo = new int[points_to_cover.length];

        //one optimization we can do here before running our algorithm is to remove 'dominated' segments - if segment A and segment B covers
        //the same points, or that segment A covers all the points covered by B and more, and B is heavier than A, then B
        //is dominated by A and can be removed

        System.out.println(optimalCoverage(points_to_cover, coverages, 0, memo));

    }
    private static int optimalCoverage(int[] points_to_cover, ArrayList<Coverage> coverages, int first_point_to_cover_index, int[] memo) {

        //base case - completed coverage
        if (first_point_to_cover_index == points_to_cover.length)
            return 0;

        //value already computed, use it
        if (memo[first_point_to_cover_index] != 0)
            return memo[first_point_to_cover_index];

        //find all segments which cover the point
        ArrayList<Coverage> relevant_covers = getRelevantCovers(coverages, points_to_cover[first_point_to_cover_index]);

        //no coverage exists
        if (relevant_covers.size() == 0)
            return -1;

        //for each possible cover, compute the optimal coverage if this cover is taken, keeping track of the overall optimal solution
        int optimal_cover = Integer.MAX_VALUE;
        boolean found_coverage = false;
        for (Coverage coverage : relevant_covers){

            //find the first point which is not covered by this segment, using binary search
            int end_point = coverage.end_point;
            int first_uncovered_point = Arrays.binarySearch(points_to_cover, end_point);
            if (first_uncovered_point < 0)
                first_uncovered_point = ~first_uncovered_point; //negative value is returned if the exact value does not exist, negate it
            else
                first_uncovered_point++; //the end point of this segments touches a point, so the first uncovered point is the next one

            //recurse to find the optimal solution if this segment is taken
            int remainder_cover = optimalCoverage(points_to_cover, coverages, first_uncovered_point, memo);

            //if there is no way to cover the remaining points, this solution is illegal
            if (remainder_cover == -1)
                continue;
            else {
                found_coverage = true;
                optimal_cover = Math.min(optimal_cover, coverage.weight + remainder_cover);
            }
        }

        //if none of the possible segments yield a complete cover, there is no solution
        if (!found_coverage)
            return memo[first_point_to_cover_index] = -1;
        //store the result before returning it
        else
            return memo[first_point_to_cover_index] = optimal_cover;
    }

    //auxilary function
    private static ArrayList<Coverage> getRelevantCovers(ArrayList<Coverage> coverages, int point_to_cover) {

        ArrayList<Coverage> covering_covers = new ArrayList<>();
        for (Coverage coverage : coverages)
            if (coverage.starting_point <= point_to_cover && coverage.end_point >= point_to_cover)
                covering_covers.add(coverage);
        return covering_covers;
    }

    //from leetcode
    public static void stickersToSpellWord() {

        //We are given N different types of stickers. Each sticker has a lowercase English word on it.
        //You would like to spell out the given target string by cutting individual letters from your collection of stickers and rearranging them.
        //You can use each sticker more than once if you want, and you have infinite quantities of each sticker.
        //What is the minimum number of stickers that you need to spell out the target? If the task is impossible, return -1.
        String[] stickers ={"with","example","science"};
        String target = "thehat";
        System.out.println(minStickers(stickers, target));

    }

    public static int minStickers(String[] stickers, String target) {

        //get rid of dominated stickers
        List<String> undominated_stickers = removeDominated(stickers, target);

        //build a similar map for the target string
        Map<Character, Integer> target_count = generateCountMap(target);

        //memoization map is a must. The key will be the remaining characters in the target according to their original location
        Map<String, Integer> memo = new HashMap<>();

        return minStickers(undominated_stickers, target, target_count, memo);
    }

    private static int minStickers(List<String> stickers, String target, Map<Character,Integer> target_count, Map<String, Integer> memo) {

        //base case - found a solution
        if (targetExhausted(target_count))
            return 0;

        //check if already computed
        String memo_key = generateMemoKey(target, target_count);
        if (memo.containsKey(memo_key))
            return memo.get(memo_key);

        //find the first character in the target string which we didn't cover with a sticker yet (must exists)
        char c = firstUncoveredCharacter(target, target_count);
        boolean found_sticker_with_character = false;

        //try each optional sticker, choosing the minimum result
        int optimal_count = Integer.MAX_VALUE;
        for (String sticker : stickers){

            int count = 0;

            //check if the sticker contains the character we want
            if (sticker.indexOf(c) != -1){

                //backup the target counter for backtracking
                Map<Character, Integer> target_count_backup = new HashMap<>(target_count);

                //apply this sticker
                count++;
                applySticker(sticker, target, target_count);

                //recurse, finding the optimal solution for the rest of the characters if there is one
                int remaining_count = minStickers(stickers, target, target_count, memo);

                //if there is a solution check if the optimal solution can be improved
                if (remaining_count != -1){
                    found_sticker_with_character = true;
                    count += remaining_count;
                    optimal_count = Math.min(optimal_count, count);
                }

                //backtrack before trying other stickers
                target_count = target_count_backup;
            }
        }
        if (!found_sticker_with_character)
            return -1;
        else{
            memo.put(memo_key, optimal_count);
            return optimal_count;
        }
    }

    private static List<String> removeDominated(String[] strings, String target){

        List<String> strings_list = new LinkedList<>(Arrays.asList(strings));

        Iterator<String> iter1 = strings_list.iterator();
        while(iter1.hasNext()){
            String s1 = iter1.next();
            boolean dominated = false;
            Iterator<String> iter2 = strings_list.iterator();
            while(iter2.hasNext()){
                String s2 = iter2.next();
                if (s1 != s2 && dominates(s2, s1, target)) {
                    dominated = true;
                    break;
                }
            }
            if (dominated)
                iter1.remove();
        }

        return strings_list;
    }

    //return true if for every character in target, count(i) <= count(j)
    private static boolean dominates(String j, String i, String target){
        Map<Character, Integer> j_map = generateCountMap(j);
        Map<Character, Integer> i_map = generateCountMap(i);
        for (char c : target.toCharArray())
            if (i_map.getOrDefault(c, 0) > j_map.getOrDefault(c, 0))
                return false;
        return true;
    }

    private static Map<Character, Integer> generateCountMap(String sticker){
        Map<Character, Integer> count_map = new HashMap<>();
        for (char c : sticker.toCharArray())
            count_map.put(c, count_map.getOrDefault(c, 0) + 1);
        return count_map;
    }

    private static boolean targetExhausted(Map<Character, Integer> count_map){
        for (int count : count_map.values())
            if (count != 0)
                return false;
        return true;
    }

    private static char firstUncoveredCharacter(String target, Map<Character, Integer> target_count){
        for (char c : target.toCharArray())
            if (target_count.get(c) != 0)
                return c;
        return 'x';
    }

    private static void applySticker(String sticker, String target,  Map<Character, Integer> target_count){
        for (char sticker_letter : sticker.toCharArray()){
            if (target.indexOf(sticker_letter) != -1){
                int remaining_appearnaces_to_cover = target_count.get(sticker_letter);
                if (remaining_appearnaces_to_cover != 0)
                    target_count.put(sticker_letter, remaining_appearnaces_to_cover - 1);
            }
        }
    }

    private static String generateMemoKey(String target, Map<Character, Integer> target_count){
        String key = "";

        //append target characters counts
        for (char target_character : target.toCharArray())
            key += target_count.get(target_character);

        return key;
    }

}
