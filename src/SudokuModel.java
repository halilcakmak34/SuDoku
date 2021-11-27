public class SudokuModel {
    //================================================================ constants
    private static final int BOARD_SIZE = 9;

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
                _board[row][col] = c - '0';  // Translate digit to int.
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
    public boolean isLegalMove(int row, int col, int val) {
        return row>=0 && row<BOARD_SIZE && col>=0 && col<BOARD_SIZE
                && val>0 && val<=9 && _board[row][col]==0;
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
}