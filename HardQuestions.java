import java.util.*;

public class HardQuestions {

    public static void main(String[] args) {

         /* All questions are from chapter 17 of 'Cracking the coding interview' - "hard questions" */
        lettersAndNumbers();

        majorityValue();

        shortestSupersequence();

        histogramVolume();
    }


    private static void lettersAndNumbers() {

        //given an array of letters an ints, find the size of the largest subarray with an equal number of numbers and letters
        Object[] input = {3,'g',3,'g',3, 3,'g','g','g',3,'g','g',3,3,3,3,'g',3,3,'g','g','g','g','g',3,'g',3,'g',3,3,'g'};

        //aux[i]: the balance of letters and numbers from the beginning of the array up to and including input[i - 1] (letter is positive, number is negative)
        int[]aux = new int[input.length + 1];
        aux[0] = 0;
        for (int i = 0; i < input.length; i++){
            if (input[i] instanceof Character)
                aux[i + 1] = aux[i] + 1;
            else
                aux[i + 1] = aux[i] + -1;
        }

        boolean simple_recursion = true;
        boolean memoization = false;
        boolean iterative = false;
        if (simple_recursion)
            System.out.println(lettersAndNumbers(aux, 0, input.length));
        else if (memoization){
            Map<String, Integer> memo = new HashMap<>();
            System.out.println(lettersAndNumbersMemo(aux, 0, input.length, memo));
        }
        else if (iterative)
            System.out.println(lettersAndNumbersIterative(aux));

    }

    //return the largest subarray in range [left, right - 1]
    private static int lettersAndNumbers(int[] aux, int left, int right) {

        if (left >= right)
            return 0;

        if (aux[left] == aux[right]) {
            System.out.println("Longest sequence of size " + (right - left) + " between indices " + left + " and " + (right - 1));
            return right - left;
        }

        return Math.max(lettersAndNumbers(aux, left + 1, right), lettersAndNumbers(aux, left, right - 1));
    }

    private static int lettersAndNumbersMemo(int[] aux, int left, int right, Map<String, Integer> memo) {
        if (left >= right)
            return 0;

        if (aux[left] == aux[right]) {
            System.out.println("Longest sequence of size " + (right - left) + " between indices " + left + " and " + (right - 1));
            return right - left;
        }

        String key = left + "_" + right;
        if (memo.containsKey(key))
            return memo.get(key);
        else{
            int value = Math.max(lettersAndNumbersMemo(aux, left + 1, right, memo), lettersAndNumbersMemo(aux, left, right - 1, memo));
            memo.put(key, value);
            return value;
        }

    }

    //this is the solution in the book, that doesn't use recursion at all - simply find the two elements in aux furthest apart which are identical
    private static int lettersAndNumbersIterative(int[] aux) {
        int max_distance = -1;
        int [] indices_of_max_distance = new int[2]; //just for printing
        Map<Integer, Integer> map = new HashMap<>(); //maps from a values in 'aux' to the first index where it was found
        for (int i = 0; i < aux.length; i++){
            if (!map.containsKey(aux[i]))
                map.put(aux[i], i);
            else {
                int index = map.get(aux[i]);
                int distance = i - index;
                if (distance > max_distance){
                    max_distance = distance;
                    indices_of_max_distance[0] = index;
                    indices_of_max_distance[1] = i - 1;
                }
            }
        }
        System.out.println("Found subarray of size " + max_distance + " between indices " + indices_of_max_distance[0] + " and " + indices_of_max_distance[1]);
        return max_distance;
    }

    //17.10
    //A majority element is an element that makes up more than half of the items in an array.
    //Given a positive integers array, find the majority element. If there is no majority element, return-1.
    //Do this inO(N) time and 0(1) space.
    public static void majorityValue() {

        int[] arr = {1, 2, 5, 9, 5, 9, 5, 5, 2};

        int candidate = 0, candidate_count = 0;
        for (int i = 0; i < arr.length; i++){
            candidate = arr[i];
            candidate_count = 1;
            while (0 < candidate_count) {
                i++;
                if (i == arr.length)
                    break;
                if (arr[i] == candidate)
                    candidate_count++;
                else
                    candidate_count--;
            }
        }

        //if there is a relevant candidate, validate it's indeed a majority value
        if (candidate_count > 0){
            candidate_count = 0;
            for (int val : arr)
                if (val == candidate)
                    candidate_count++;
            if (candidate_count > (arr.length / 2)) {
                System.out.println(candidate + " is a majority element");
                return;
            }
        }
        System.out.println("There is no majority element");
    }

    //17.8
    //You are given two arrays, one shorter (with all distinct elements) and one longer.
    //Find the shortest subarray in the longer array that contains all the elements in the shorter array.
    // The items can appear in any order
    public static void shortestSupersequence() {

        //expected answer here is 7, 10
        int[] small_array = {7, 0};
        int[] big_array = {7,5,9,0,2,1,3,5,7,9,1,1,5,8,8,9,7};

        shortestSupersequence(small_array, big_array);
    }

    private static void shortestSupersequence(int[] s, int[] b) {

        //semi brute force:
        //scan the array inserting the elements into a hashmap (maps from element to its position), until all elements
        //are found (if they are not, return null). During the scan, if an element is found again, update it to its newer location.
        //we now have a basic range, which we'll try to improve.
        //start another scan:
        //Remove the first element in the set (the one with the lowest index), and check what is the new result by looking for it
        //in the part of the array beyond our range. if there is none it means the element was not found again, and we cannot
        //improve our answer, return the best solution so far. If there is a solution (even if it's not better
        //than what we have) than repeat for the next element. Do it until the array is exhausted.

        //The algorithm is basically similar, but clearer and more efficient:
        //do one scan where you store all indices of each element from the small array in an Arraylist:
        //1 -> 5, 10, 11
        //5 -> 1, 7, 12
        //9 -> 2, 9, 15
        //now it's simple: our initial range is [1,5]. Try to improve by pulling out 1, and get the next index of this element, in
        //this case 7 (if one of the lists is exhausted, then we cannot improve further, stop). we now have [2,7]. pull 2, insert 9,
        //and so on. This is similar to the 'merge lists' problem, which uses a heap to find the minimum element. We'll store
        //in the heap pairs which contain the element itself and its index (e.g. {1,5}, {5,1}, {9,2}
        //run time is O(B) to construct the lists, and O(B*log(S)) for the scan in worst case.
        //Space complexity is O(S) for this lists and for the heap

        ArrayList<Integer>[] lists = createAppearancesLists(s, b);

        PriorityQueue<HeapNode> min_heap = new PriorityQueue<>(
                new Comparator<HeapNode>() {
                    @Override
                    public int compare(HeapNode a, HeapNode b) {
                        return lists[a.list_id].get(a.index_in_list) - lists[b.list_id].get(b.index_in_list);
                    }
                });


        //add first element from each list to the heap, while keeping track of the maximal value
        int max_index = Integer.MIN_VALUE;
        for (int i = 0; i < lists.length; i++){
            if (lists[i].size() == 0){
                System.out.println("Element " + s[i] + " does not appear in the large array, cannot compute range");
                return;
            }
            int list_id = i;
            int index_in_list = 0;
            min_heap.add(new HeapNode(list_id, index_in_list));
            max_index = Math.max(max_index, lists[list_id].get(index_in_list));
        }

        //scan the lists
        int start_shortest_index = -1, end_shortest_index = -1, shortest_range = Integer.MAX_VALUE;
        while (true) {
            HeapNode node = min_heap.remove();
            int index = lists[node.list_id].get(node.index_in_list);
            int range = max_index - index;
            if (range < shortest_range) {
                start_shortest_index = index;
                end_shortest_index = max_index;
                shortest_range = range; //easy to forget!
            }
            //once one of the list is exhausted, we cannot further improve our solution
            if (node.index_in_list == lists[node.list_id].size() - 1)
                break;
            else {
                int new_index = node.index_in_list + 1;
                min_heap.add(new HeapNode(node.list_id, new_index));
                max_index = Math.max(max_index, lists[node.list_id].get(new_index));
            }
        }
        System.out.println("[" + start_shortest_index + "," + end_shortest_index + "]");
    }

    private static ArrayList<Integer>[] createAppearancesLists(int[] s, int[] b) {
        ArrayList<Integer>[] lists = new ArrayList[s.length];
        for (int i = 0; i < s.length; i++) {
            lists[i] = new ArrayList<>();
            for (int j = 0; j < b.length; j++)
                if (s[i] == b[j])
                    lists[i].add(j);
        }
        return lists;
    }

    //17.21
    //Imagine a histogram (bar graph). Design an algorithm to compute the volume of water it could hold if someone poured water across the top.
    //You can assume that each histogram bar has width 1.
    //input:{0, 0, 4, 0, 0, 6, 0, 0, 3, 0, 5, 0, 1, 0, 0, 0} -> 26
    public static void histogramVolume() {

        int[] input = {0, 0, 4, 0, 0, 6, 0, 0, 3, 0, 5, 0, 1, 0, 0, 0};
        //A tricky question at first but actually easy: we can know the level of water at each point based on:
        //(1) the height of the bar at this point (if there is one)
        //(2) the highest bar looking left from this location (zero if there is no bar to the left)
        //(3) the highest bar looking right from this location (zero if there is no bar to the right)
        //the amount of water is min{2,3} - (1). If negative discard (it means that there is a bar at this location and it's higher than one of its neighbours)

        //sweep right keeping track of the highest bar so far
        int[] highest_to_the_left = new int[input.length];
        int highest = 0;
        for (int i = 0; i < input.length; i++)
            highest_to_the_left[i] = highest = Math.max(highest, input[i]);

        //sweep to the left creating similar data. Use this sweep to compute the volume at this point
        int[] highest_to_the_right = new int[input.length];
        highest = 0;
        int total_volume = 0;
        for (int i = input.length - 1; i >= 0; i--) {
            highest_to_the_right[i] = highest = Math.max(highest, input[i]);
            int min = Math.min(highest_to_the_left[i], highest_to_the_right[i]);
            int volume = (min > input[i])? min - input[i] : 0;
            total_volume += volume;
        }

        System.out.println(total_volume);
    }
}
