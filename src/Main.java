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

    public static void mergeSort(Point[] points, int n, boolean sortByX) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Point[] left = new Point[mid];
        Point[] right = new Point[n - mid];

        // Splitting the array into two halves
        for (int i = 0; i < mid; i++) {
            left[i] = points[i];
        }
        for (int i = mid; i < n; i++) {
            right[i - mid] = points[i];
        }

        // Recursively sorting both halves
        mergeSort(left, mid, sortByX);
        mergeSort(right, n - mid, sortByX);

        // Merging the sorted halves
        merge(points, left, right, mid, n - mid, sortByX);
    }

    public static void merge(Point[] points, Point[] left, Point[] right, int leftSize, int rightSize, boolean sortByX) {
        int i = 0, j = 0, k = 0;

        while (i < leftSize && j < rightSize) {
            if (sortByX) {
                // Sort by x coordinate
                if (left[i].x < right[j].x || (left[i].x == right[j].x && left[i].y <= right[j].y)) {
                    points[k++] = left[i++];
                } else {
                    points[k++] = right[j++];
                }
            } else {
                // Sort by y coordinate
                if (left[i].y < right[j].y || (left[i].y == right[j].y && left[i].x <= right[j].x)) {
                    points[k++] = left[i++];
                } else {
                    points[k++] = right[j++];
                }
            }
        }

        // Copy remaining elements
        while (i < leftSize) {
            points[k++] = left[i++];
        }
        while (j < rightSize) {
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
        mergeSort(pointsX, points.length, true);

        // Sort points by y-coordinate using merge sort
        Point[] pointsY = Arrays.copyOf(points, points.length);
        mergeSort(pointsY, points.length, false);

        // Find the closest pair of points
        double closestDist = closestPair(pointsX, pointsY);
        System.out.println("The closest pair of points has a distance of: " + closestDist);
    }
}
