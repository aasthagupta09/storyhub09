import java.util.ArrayList;
import java.util.Scanner;

class Story {
    int id;
    String title;
    String author;
    String category;
    String content;
    double rating;

    Story(int id, String title, String author, String category, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.content = content;
        this.rating = 0;
    }

    void displayStory() {
        System.out.println("\n-----------------------------");
        System.out.println("Title    : " + title);
        System.out.println("Author   : " + author);
        System.out.println("Category : " + category);
        System.out.println("Rating   : " + rating + "/5");
        System.out.println("Story    : " + content);
        System.out.println("-----------------------------");
    }
}

public class StoryHubBackend {

    static ArrayList<Story> stories = new ArrayList<>();

    public static void main(String[] args) {
        addSampleStories();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== STORYHUB BACKEND =====");
            System.out.println("1. Show all stories");
            System.out.println("2. Publish story");
            System.out.println("3. Search story");
            System.out.println("4. Rate story");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showStories();
                    break;
                case 2:
                    publishStory(sc);
                    break;
                case 3:
                    searchStory(sc);
                    break;
                case 4:
                    rateStory(sc);
                    break;
                case 5:
                    System.out.println("Thank you for using StoryHub!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        sc.close();
    }

    static void addSampleStories() {
        stories.add(new Story(1, "The Last Letter", "Aarav", "Romance",
                "A letter written ten years ago finally reaches its destination."));
        stories.add(new Story(2, "Midnight Mystery", "Riya", "Mystery",
                "Every night at midnight, the light in Room 13 turns on."));
        stories.add(new Story(3, "The Hidden Kingdom", "Kabir", "Fantasy",
                "Behind an ordinary forest is a magical kingdom."));
    }

    static void showStories() {
        if (stories.isEmpty()) {
            System.out.println("No stories available.");
            return;
        }

        for (Story story : stories) {
            story.displayStory();
        }
    }

    static void publishStory(Scanner sc) {
        System.out.print("Enter story title: ");
        String title = sc.nextLine();

        System.out.print("Enter author name: ");
        String author = sc.nextLine();

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter story content: ");
        String content = sc.nextLine();

        Story story = new Story(
                stories.size() + 1,
                title,
                author,
                category,
                content
        );

        stories.add(story);
        System.out.println("Story published successfully!");
    }

    static void searchStory(Scanner sc) {
        System.out.print("Enter story title: ");
        String keyword = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Story story : stories) {
            if (story.title.toLowerCase().contains(keyword)) {
                story.displayStory();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Story not found.");
        }
    }

    static void rateStory(Scanner sc) {
        System.out.print("Enter story ID: ");
        int id = sc.nextInt();

        System.out.print("Enter rating (1-5): ");
        double rating = sc.nextDouble();

        for (Story story : stories) {
            if (story.id == id) {
                if (rating >= 1 && rating <= 5) {
                    story.rating = rating;
                    System.out.println("Rating saved!");
                } else {
                    System.out.println("Rating must be between 1 and 5.");
                }
                return;
            }
        }

        System.out.println("Story not found.");
    }
}
