import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MagicSquare {

    private static final String relativePathStringToTxt = "/src/P1/txt/";

    public static void main(String[] args) throws IOException {
        testTheGivenSquares();

        if (generateMagicSquare(9)) {
            String fileName = "6.txt";
            if (isLegalMagicSquare(fileName))
                System.out.printf("%s is a legal magic square.\n", fileName);
            else
                System.out.printf("%s is not a legal magic square.\n", fileName);
        }
    }

    public static void testTheGivenSquares() throws IOException {

        List<String> fileNames = new ArrayList<>();
        fileNames.add("1.txt");
        fileNames.add("2.txt");
        fileNames.add("3.txt");
        fileNames.add("4.txt");
        fileNames.add("5.txt");

        for (String fileName : fileNames) {
            if (isLegalMagicSquare(fileName)) System.out.printf("%s is a legal magic square.\n", fileName);
            else System.out.printf("%s is not a legal magic square.\n", fileName);
            System.out.println();
        }

    }

    public static boolean isLegalMagicSquare(String fileName) {
        String filePathString = getFullPathString(relativePathStringToTxt, fileName);
        int lengthOfSide;
        List<String> squareStringsBuffer = new ArrayList<>();
        Path filePath = Paths.get(filePathString);
        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(squareStringsBuffer::add);
        } catch (IOException e) {
            System.out.println("Read file error! Make sure you are running the program at the right working directory.");
        }


        lengthOfSide = squareStringsBuffer.size();
        int[][] square = new int[lengthOfSide][lengthOfSide];

        for (int i = 0; i < lengthOfSide; i++) {
            String[] lineStringNumbers = squareStringsBuffer.get(i).split("\t");
            int lineStringNumberCnt = 0;
            for (String lineStringNumber : lineStringNumbers) {
                if (lineStringNumberCnt == lengthOfSide) {
                    System.out.printf("Too many numbers at line %d.\n", i);
                    return false;
                }
                if (isNotPositiveInteger(lineStringNumber)) {
                    System.out.printf("The number [%s] at [%d, %d] is not a positive integer " +
                            "or there are some numbers split without '\\t'.\n", lineStringNumber, i, lineStringNumberCnt);
                    return false;
                }
                square[i][lineStringNumberCnt] = Integer.parseInt(lineStringNumber);
                lineStringNumberCnt++;
            }
            if (lineStringNumberCnt < lengthOfSide) {
                System.out.printf("Too few numbers at line %d.\n", i);
                return false;
            }
        }

        int squareValue = 0;
        for (int i = 0; i < lengthOfSide; i++) {
            squareValue += square[0][i];
        }

        for (int i = 1; i < lengthOfSide; i++) {
            int currentRowValue = 0;
            for (int j = 0; j < lengthOfSide; j++) {
                currentRowValue += square[i][j];
            }
            if (currentRowValue != squareValue) {
                System.out.printf("The sum of row %d does not equal to that of the 1st row.\n", i);
                return false;
            }
        }

        for (int j = 0; j < lengthOfSide; j++) {
            int currenColValue = 0;
            for (int i = 0; i < lengthOfSide; i++) {
                currenColValue += square[i][j];
            }
            if (currenColValue != squareValue) {
                System.out.printf("The sum of column %d does not equal to that of the 1st row.\n", j);
                return false;
            }
        }

        int diagonalValue = 0;
        for (int i = 0; i < lengthOfSide; i++) {
            diagonalValue += square[i][i];
        }
        if (diagonalValue != squareValue) {
            System.out.println("The sum of the main diagonal does noe equal to that of the 1st row.");
        }

        diagonalValue = 0;
        for (int i = 0; i < lengthOfSide; i++) {
            diagonalValue += square[lengthOfSide - i - 1][i];
        }
        if (diagonalValue != squareValue) {
            System.out.println("The sum of the main diagonal does noe equal to that of the 1st row.");
        }

//        System.out.printf("The sum of the 1st row of the square is %d\n", squareValue);

        return true;
    }

    public static boolean isNotPositiveInteger(String numberString) {
        int value = -1;
        try {
            value = Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            return true;
        }
        return value < 1;
    }

    public static boolean generateMagicSquare(int n) {

        if (n % 2 == 0) {
            System.out.println("It is illegal to pass an odd number to generate a magic square.");
            return false;
        }

        if (n < 0) {
            System.out.println("It is illegal to pass an negative number to generate a magic square.");
            return false;
        }

        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;
        for (i = 1; i <= square; i++) { // 每次向方阵中填一个数，迭代 n * n 次
            magic[row][col] = i;
            if (i % n == 0) // 如果左上方已经填入一个数，即 i 可以被 n 整除，则从下一行开始填
                row++;
            else {  // 否则下一个数的位置是当前数的左上方
                if (row == 0)   // 若已经到方阵顶端，则下一个数在方阵底端，否则在当前行的上一行
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1)) // 若已经到达方阵右端，则下一个数在方阵最左端，否则在当前列的右一列
                    col = 0;
                else
                    col++;
            }
        }
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }

        try (PrintWriter out = new PrintWriter(getFullPathString(relativePathStringToTxt, "6.txt"))) {
            for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++)
                    out.print(magic[i][j] + "\t");
                out.println();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Can not find file %s\n", getFullPathString(relativePathStringToTxt, "6.txt"));
        }

        return true;
    }

    public static String getFullPathString(String relativeDirPathString, String fileName) {
        String rootDir = System.getProperty("user.dir");
        return (Path.of(rootDir + relativeDirPathString + fileName).normalize().toString());
    }

}
