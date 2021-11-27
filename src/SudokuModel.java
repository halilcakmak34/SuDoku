import java.util.stream.IntStream;

public class SudokuModel {
    //================================================================ constants
    public static final int BOARD_SIZE = 9;
    public static final int BOARD_START_INDEX=0;
    public static int NO_VALUE = 0;
    private static int SUBSECTION_SIZE = 3;
    public static int MIN_VALUE = 1;
    public static int MAX_VALUE = 9;

    //=================================================================== fields
    private int[][] _board;

    //============================================================== constructor
    public SudokuModel() {
        _board = new int[BOARD_SIZE][BOARD_SIZE];
    }

    //============================================================== constructor
    public SudokuModel(String initialBoard) {
        this();       // Call no parameter constructor first.
        initializeFromString(initialBoard);
    }

    //===================================================== initializeFromString
    public void initializeFromString(final String boardStr) {
        clear();  // Clear all values from the board.
        int row = 0;
        int col = 0;
        //... Loop over every character.
        for (int i = 0; i < boardStr.length(); i++) {
            char c = boardStr.charAt(i);
            if (c >= '1' && c <='9') {
                if (row > BOARD_SIZE || col > BOARD_SIZE) {
                    throw new IllegalArgumentException("SudokuModel: "
                            + " Attempt to initialize outside 1-9 "
                            + " at row " + (row+1) + " and col " + (col+1));
                }
                _board[row][col] = Integer.valueOf(String.valueOf(c)).intValue();  // c-'0'; Translate digit to int.
                col++;
            } else if (c == '.') {
                col++;
            } else if (c == '/') {
                row++;
                col = 0;
            } else {
                throw new IllegalArgumentException("SudokuModel: Character '" + c
                        + "' not allowed in board specification");
            }
        }
    }

    //============================================================== islegalMove
    public boolean isLegalMove(int row, int col) {
        return isValid(_board,row,col);//row>=0 && row<BOARD_SIZE && col>=0 && col<BOARD_SIZE && val>0 && val<=9 && _board[row][col]==0;
    }

    //============================================================== islegalMove
    public boolean isLegalMove(int row, int col, int val) {
        return row>=0 && row<BOARD_SIZE && col>=0 && col<BOARD_SIZE && val>0 && val<=9 && _board[row][col]==0;
    }

    //=================================================================== setVal
    public void setVal(int r, int c, int v) {
        _board[r][c] = v;
    }

    //=================================================================== getVal
    public int getVal(int row, int col) {
        return _board[row][col];
    }

    //===================================================================== clear
    public void clear() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                setVal(row, col, 0);
            }
        }
    }

    public boolean solve() {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (_board[row][column] == NO_VALUE) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        _board[row][column] = k;
                        if (isValid(_board, row, column) && solve()) {
                            return true;
                        }
                        //_board[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (rowConstraint(board, row)
                && columnConstraint(board, column)
                && subsectionConstraint(board, row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX+1, BOARD_SIZE)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX+1, BOARD_SIZE)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;
        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    boolean checkConstraint(
            int[][] board,
            int row,
            boolean[] constraint,
            int column) {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void printBoard() {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                System.out.print(_board[row][column] + " ");
            }
            System.out.println();
        }
    }
}