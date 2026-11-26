import java.util.*;

public class OnlineQuizSystem {
    static class Question {
        int id;
        String text;
        String[] options;
        int correct;

        Question(int id, String text, String[] options, int correct) {
            this.id = id;
            this.text = text;
            this.options = options;
            this.correct = correct;
        }

        void show() {
            System.out.println("\n" + id + ". " + text);
            for (int i = 0; i < options.length; i++) {
                System.out.print((i + 1) + ". " + options[i] + "   ");
            }
            System.out.println();
        }
    }

    static class StudentResult {
        String name;
        int score;

        StudentResult(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    static ArrayList<Question> questions = new ArrayList<>();
    static HashMap<String, Integer> results = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    static void add(int id, String text, int correct, String... options) {
        questions.add(new Question(id, text, options, correct));
    }

    // Questions are added in ascending ID order (needed for Binary Search)
    static void loadQuestions() {
        add(1, "Which data structure follows FIFO?", 2, "Stack", "Queue", "Tree", "Graph");
        add(2, "Which data structure follows LIFO?", 1, "Stack", "Queue", "Array", "Graph");
        add(3, "Time complexity of Binary Search is?", 3, "O(n)", "O(n^2)", "O(log n)", "O(1)");
        add(4, "Which algorithm uses divide and conquer?", 4, "Linear Search", "Bubble Sort", "Insertion Sort", "Merge Sort");
        add(5, "Which class stores key-value pairs in Java?", 2, "ArrayList", "HashMap", "Scanner", "String");
    }

    static String readLine() {
        if (!sc.hasNextLine()) {
            System.out.println("\nInput closed. Exiting.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // Keeps asking until a whole number in [min, max] is entered
    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(readLine());
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            System.out.println("Invalid input. Please try again.");
        }
    }

    static void startQuiz() {
        System.out.print("Enter student name: ");
        String name = readLine();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter student name: ");
            name = readLine();
        }
        int score = 0;
        for (Question q : questions) {
            q.show();
            int answer = readInt("Enter option (1-4): ", 1, 4);
            if (answer == q.correct) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong! Correct answer: " + q.options[q.correct - 1]);
            }
        }
        results.put(name, score); // latest score of a student is kept
        System.out.println("\n" + name + ", your score is " + score + "/" + questions.size());
    }

    static Question binarySearch(int id) {
        int low = 0, high = questions.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Question q = questions.get(mid);
            if (q.id == id) return q;
            if (q.id < id) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    static void searchQuestion() {
        int id = readInt("Enter question ID: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        Question q = binarySearch(id);
        if (q == null) System.out.println("Question not found.");
        else q.show();
    }

    // Sorts in descending order of score
    static void mergeSort(StudentResult[] a, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, right);
    }

    static void merge(StudentResult[] a, int left, int mid, int right) {
        StudentResult[] temp = new StudentResult[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            temp[k++] = (a[i].score >= a[j].score) ? a[i++] : a[j++];
        }
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];
        System.arraycopy(temp, 0, a, left, temp.length);
    }

    static void showLeaderboard() {
        if (results.isEmpty()) {
            System.out.println("No results yet. Attempt the quiz first.");
            return;
        }
        StudentResult[] list = new StudentResult[results.size()];
        int i = 0;
        for (Map.Entry<String, Integer> e : results.entrySet()) {
            list[i++] = new StudentResult(e.getKey(), e.getValue());
        }
        mergeSort(list, 0, list.length - 1);
        System.out.println("\n----- LEADERBOARD -----");
        for (int r = 0; r < list.length; r++) {
            System.out.println((r + 1) + ". " + list[r].name + " - " + list[r].score);
        }
    }

    public static void main(String[] args) {
        loadQuestions();
        while (true) {
            System.out.println("\n===== ONLINE QUIZ SYSTEM =====");
            System.out.println("1. Start Quiz");
            System.out.println("2. Search Question by ID");
            System.out.println("3. View Leaderboard");
            System.out.println("4. Exit");
            switch (readInt("Choose option: ", 1, 4)) {
                case 1: startQuiz(); break;
                case 2: searchQuestion(); break;
                case 3: showLeaderboard(); break;
                default:
                    System.out.println("Thank you!");
                    return;
            }
        }
    }
}
