import java.io.*;
import java.util.*;

/**
 * Handles persistent storage and retrieval of voting system data.
 */
public class DataStore {

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin123";

    private static final String CANDIDATE_FILE = "candidates.txt";
    private static final String VOTERS_FILE = "voters.txt";
    private static final String VOTED_FILE = "voted.txt";

    /**
     * Loads all candidates from the data file.
     * Format: name,votes,symbol
     *
     * @return list of candidate data as String arrays
     */
    public static List<String[]> loadCandidates() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CANDIDATE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length < 3)
                    parts = new String[]{parts[0], parts.length > 1 ? parts[1] : "0", ""};
                data.add(parts);
            }
        } catch (IOException e) {
            System.err.println("Error loading candidates: " + e.getMessage());
        }
        return data;
    }

    /**
     * Saves candidate data to file.
     *
     * @param candidates list of candidate records
     */
    public static void saveCandidates(List<String[]> candidates) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CANDIDATE_FILE))) {
            for (String[] c : candidates)
                pw.println(String.join(",", c));
        } catch (IOException e) {
            System.err.println("Error saving candidates: " + e.getMessage());
        }
    }

    /**
     * Loads all registered voters.
     * Format: username,hashedPassword
     *
     * @return map of username to hashed password
     */
    public static Map<String, String> loadVoters() {
        Map<String, String> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(VOTERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) map.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.err.println("Error loading voters: " + e.getMessage());
        }
        return map;
    }

    /**
     * Saves all voter records to file.
     *
     * @param voters map of username to hashed password
     */
    public static void saveVoters(Map<String, String> voters) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VOTERS_FILE))) {
            for (Map.Entry<String, String> entry : voters.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving voters: " + e.getMessage());
        }
    }

    /**
     * Loads usernames who have voted.
     *
     * @return list of usernames
     */
    public static List<String> loadVotedUsers() {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(VOTED_FILE))) {
            String line;
            while ((line = br.readLine()) != null)
                list.add(line.trim());
        } catch (IOException e) {
            System.err.println("Error loading voted users: " + e.getMessage());
        }
        return list;
    }

    /**
     * Saves the full voted user list to file.
     *
     * @param votedUsers list of usernames
     */
    public static void saveVotedUsers(List<String> votedUsers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VOTED_FILE))) {
            for (String user : votedUsers) {
                writer.println(user);
            }
        } catch (IOException e) {
            System.err.println("Error saving voted users: " + e.getMessage());
        }
    }

    /**
     * Appends a username to the voted list.
     *
     * @param name the username who has just voted
     */
    public static void markUserVoted(String name) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(VOTED_FILE, true))) {
            bw.write(name + "\n");
        } catch (IOException e) {
            System.err.println("Error appending voted user: " + e.getMessage());
        }
    }
}
