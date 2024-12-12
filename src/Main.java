import java.awt.Point;
import java.util.*;

public class Main {

    // Point class to represent a point (x, y)
    static class Point {
        int x, y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Function to calculate the Euclidean distance between two points
    public static double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    // Merge sort for sorting points based on x-coordinate
    public static void mergeSortByX(Point[] points, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSortByX(points, low, mid);
            mergeSortByX(points, mid + 1, high);
            mergeByX(points, low, mid, high);
        }
    }

    // Merge function for merge sort (based on x-coordinate)
    public static void mergeByX(Point[] points, int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;

        Point[] left = new Point[n1];
        Point[] right = new Point[n2];

        System.arraycopy(points, low, left, 0, n1);
        System.arraycopy(points, mid + 1, right, 0, n2);

        int i = 0, j = 0, k = low;
        while (i < n1 && j < n2) {
            if (left[i].x <= right[j].x) {
                points[k++] = left[i++];
            } else {
                points[k++] = right[j++];
            }
        }

        while (i < n1) {
            points[k++] = left[i++];
        }

        while (j < n2) {
            points[k++] = right[j++];
        }
    }

    // Merge sort for sorting points based on y-coordinate
    public static void mergeSortByY(Point[] points, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSortByY(points, low, mid);
            mergeSortByY(points, mid + 1, high);
            mergeByY(points, low, mid, high);
        }
    }

    // Merge function for merge sort (based on y-coordinate)
    public static void mergeByY(Point[] points, int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;

        Point[] left = new Point[n1];
        Point[] right = new Point[n2];

        System.arraycopy(points, low, left, 0, n1);
        System.arraycopy(points, mid + 1, right, 0, n2);

        int i = 0, j = 0, k = low;
        while (i < n1 && j < n2) {
            if (left[i].y <= right[j].y) {
                points[k++] = left[i++];
            } else {
                points[k++] = right[j++];
            }
        }

        while (i < n1) {
            points[k++] = left[i++];
        }

        while (j < n2) {
            points[k++] = right[j++];
        }
    }

    // Function to find the closest pair of points using divide and conquer
    public static double closestPair(Point[] pointsX, Point[] pointsY) {
        int n = pointsX.length;

        // Base cases
        if (n == 2) {
            return dist(pointsX[0], pointsX[1]);
        }
        if (n == 3) {
            return Math.min(Math.min(dist(pointsX[0], pointsX[1]), dist(pointsX[0], pointsX[2])), dist(pointsX[1], pointsX[2]));
        }

        // Divide
        int mid = n / 2;
        Point midPoint = pointsX[mid];

        // Recursively find the closest pair in the left and right halves
        double dl = closestPair(Arrays.copyOfRange(pointsX, 0, mid), Arrays.copyOfRange(pointsY, 0, mid));
        double dr = closestPair(Arrays.copyOfRange(pointsX, mid, n), Arrays.copyOfRange(pointsY, mid, n));
        double d = Math.min(dl, dr);

        // Conquer step: find the closest pair that crosses the middle line
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsY) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && strip.get(j).y - strip.get(i).y < d; j++) {
                d = Math.min(d, dist(strip.get(i), strip.get(j)));
            }
        }

        return d;
    }

    // Main function to run the closestPair algorithm
    public static void main(String[] args) {
        // Example points (x, y)
        Point[] points = {
                new Point(2, 3),
                new Point(12, 30),
                new Point(40, 50),
                new Point(5, 1),
                new Point(12, 10),
                new Point(3, 4)
        };

        // Sort points by x-coordinate using merge sort
        Point[] pointsX = Arrays.copyOf(points, points.length);
        mergeSortByX(pointsX, 0, pointsX.length - 1);

        // Sort points by y-coordinate using merge sort
        Point[] pointsY = Arrays.copyOf(points, points.length);
        mergeSortByY(pointsY, 0, pointsY.length - 1);

        // Find the closest pair of points
        double closestDist = closestPair(pointsX, pointsY);
        System.out.println("The closest pair of points has a distance of: " + closestDist);
    }
}
