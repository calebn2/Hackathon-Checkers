import java.util.Scanner;

public class CheckersGameRunner {
    private static final int BOARD_SIZE = 8;
    private static final char EMPTY = '-';
    private static final char RED = 'r';
    private static final char BLACK = 'b';
    private static final int[][] RED_MOVES = {{-1, -1}, {-1, 1}};
    private static final int[][] BLACK_MOVES = {{1, -1}, {1, 1}};
    private static final int[][] KING_MOVES = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    private char[][] board;
    private boolean isRedTurn;

    public CheckersGameRunner() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        isRedTurn = true;
        initializeBoard();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();
            System.out.println((isRedTurn ? "Red" : "Black") + "'s turn");
            System.out.print("Enter the current row: ");
            int currentRow = scanner.nextInt();
            System.out.print("Enter the current column: ");
            int currentCol = scanner.nextInt();
            System.out.print("Enter the new row: ");
            int newRow = scanner.nextInt();
            System.out.print("Enter the new column: ");
            int newCol = scanner.nextInt();

            if (isValidMove(currentRow, currentCol, newRow, newCol)) {
                makeMove(currentRow, currentCol, newRow, newCol);
                if (isRedTurn && newRow == 0) {
                    CheckersGame.crownKing(newRow, newCol);
                } else if (!isRedTurn && newRow == BOARD_SIZE - 1) {
                    CheckersGame.crownKing(newRow, newCol);
                }
                if (hasCaptureMove()) {
                    while(true) {
                        System.out.print((isRedTurn ? "Red" : "Black") + " has a capture move. Do you want to continue your turn? y/n: ");
                        String yn = scanner.nextLine();
                        if (yn.equals("y")) {
                            break;
                        } else if (yn.equals("n")) {
                            isRedTurn = !isRedTurn;
                            break;
                        } else {
                            System.out.println("You did not enter a valid answer. Enter either y or n.");
                        }
                    }
                }
                else 
                    isRedTurn = !isRedTurn;
            } else {
                System.out.println("Invalid move!");
            }

            if (CheckersGame.isGameOver()) {
                printBoard();
                System.out.println((isRedTurn ? "Black" : "Red") + " wins!");
                break;
            }
        }
        scanner.close();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3) {
                        board[row][col] = BLACK;
                    } else if (row > BOARD_SIZE - 4) {
                        board[row][col] = RED;
                    } else {
                        board[row][col] = EMPTY;
                    }
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    private void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int row = 0; row < BOARD_SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidMove(int currentRow, int currentCol, int newRow, int newCol) {
        if (!isValidPosition(currentRow, currentCol) || !isValidPosition(newRow, newCol))
            return false;

        char piece = board[currentRow][currentCol];
        if ((piece == RED && !isRedTurn) || (piece == BLACK && isRedTurn))
            return false;

        int[][] moves = (isRedTurn) ? RED_MOVES : BLACK_MOVES;
        int deltaRow = newRow - currentRow;
        int deltaCol = newCol - currentCol;

        if (Character.isUpperCase(piece)) moves = KING_MOVES;

        if ((Math.abs(deltaRow) != 1 || Math.abs(deltaCol) != 1) && !(Math.abs(deltaRow) == 2 && Math.abs(deltaCol) == 2)) {
            return false;
        }

        if (Math.abs(deltaRow) == 2 && Math.abs(deltaCol) == 2) {
            if (jumpMove(currentRow, currentCol, newRow, newCol, deltaRow, deltaCol, moves))
                return true;
            return false;
        }

        for (int[] move : moves) {
            if (deltaRow == move[0] && deltaCol == move[1]) {
                if (board[newRow][newCol] == EMPTY)
                    return true;
                else {
                    return false;
                }
            }
        }
        return false;
    }

    private void makeMove(int currentRow, int currentCol, int newRow, int newCol) {
        char piece = board[currentRow][currentCol];
        board[currentRow][currentCol] = EMPTY;
        board[newRow][newCol] = piece;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    private boolean jumpMove(int currentRow, int currentCol, int newRow, int newCol, int deltaRow, int deltaCol, int[][] moves) {
        char piece = board[currentRow][currentCol];
        deltaRow = (deltaRow > 0 ? 1 : -1);
        deltaCol = (deltaCol > 0 ? 1 : -1);

        for (int[] move : moves) {
            if (deltaRow == move[0] && deltaCol == move[1]) {
                char jumpedPiece = board[currentRow + deltaRow][currentCol + deltaCol];
                System.out.println("row: " + (currentRow + deltaRow));
                System.out.println("column: " + (currentCol + deltaCol));
                System.out.println("jumpedPiece = " + jumpedPiece);
                if (jumpedPiece != piece && jumpedPiece != EMPTY) {
                    board[currentRow + deltaRow][currentCol + deltaCol] = EMPTY;
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean hasCaptureMove() {
        char playerPiece = isRedTurn ? RED : BLACK;
    
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == playerPiece || board[row][col] == Character.toUpperCase(playerPiece)) {
                    int[][] moves = isRedTurn ? RED_MOVES : BLACK_MOVES;
    
                    for (int[] move : moves) {
                        int capturedRow = row + move[0];
                        int capturedCol = col + move[1];
                        int newRow = row + 2 * move[0];
                        int newCol = col + 2 * move[1];
    
                        if (isValidPosition(newRow, newCol) && board[newRow][newCol] == EMPTY
                                && isValidPosition(capturedRow, capturedCol)
                                && isOpponentPiece(board[capturedRow][capturedCol])) {
                            return true;
                        }
                    }
                }
            }
        }
    
        return false;
    }

    private boolean isOpponentPiece(char piece) {
        return (isRedTurn && piece == BLACK) || (!isRedTurn && piece == RED);
    }

    public static void main(String[] args) {
        CheckersGameRunner game = new CheckersGameRunner();
        game.play();
    }
}