import java.util.Arrays;
import java.util.stream.IntStream;

public class SudokuModel {
    //================================================================ constants
    public static final int BOARD_SIZE = 21;
    public static final int BOARD_START_INDEX=0;
    public static final int PUZZLE_LENGTH = 9;
    public static int puzzle = 1;
    public static int NO_VALUE = 0;
    public static int NEGATIVE_VALUE = -1;
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
                _board[row][col] = 0;
                col++;
            }else if (c == 'X') {
                _board[row][col] = -1;
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

    public boolean isLegalMove2(int row, int col, int val) {
        return isValid2(_board,row,col,val);//row>=0 && row<BOARD_SIZE && col>=0 && col<BOARD_SIZE && val>0 && val<=9 && _board[row][col]==0;
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
                        _board[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {

        return (rowConstraint(_board, row)
                && columnConstraint(_board, column)
                && subsectionConstraint(_board, row, column));
    }

    private boolean isValid2(int[][] board, int row, int column,int val) {

        return (rowConstraint2(row,column,val)
                && columnConstraint2(row, column,val)
                && subsectionConstraint2(row,column,val));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];

        boolean returnValue =  IntStream.range(BOARD_START_INDEX+1, BOARD_SIZE)
                .allMatch(column ->checkConstraint(_board, row, constraint, column));

        return returnValue;
    }
    private boolean rowConstraint2(int row,int column, int val) {
        int[] constraintValue = initConstraintValue();
        int puzzle = 5;
        if (row < 9 && column < 9) {
            puzzle = 1;
        }

        if(row>=12 && column>=12){
            puzzle = 4;
        }

        if(row>=12 && column<9){
            puzzle = 3;
        }

        if(row<9 && column>=12){
            puzzle = 2;
        }

        int start = -1;

        switch (puzzle){
            case 1:
                start = 0;
                break;
            case 2:
                start = 12;
                break;
            case 3:
                start = 0;
                break;
            case 4:
                start = 12;
                break;
            case 5:
                start = 9;
        }


        int finalStart = start;
        boolean returnValue =  IntStream.range(BOARD_START_INDEX, PUZZLE_LENGTH-1)
                .allMatch(index ->checkConstraintRow(constraintValue, finalStart +index, column,index,val));

        for(int i =0;i<constraintValue.length;i++){
            if(constraintValue[i]>1){
                return false;
            }
        }

        return true;
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        boolean returnValue = IntStream.range(BOARD_START_INDEX+1, BOARD_SIZE)
                .allMatch(row ->checkConstraint(_board, row, constraint, column));

        return returnValue;
    }

    private boolean columnConstraint2(int row, int column, int val) {
        int[] constraintValue = initConstraintValue();

        int puzzle = -1;
        if (row < 9 && column < 9) {
            puzzle = 1;
        }

        if(row>=12 && column>=12){
            puzzle = 4;
        }

        if(row>=12 && column<9){
            puzzle = 3;
        }

        if(row<9 && column>=12){
            puzzle = 2;
        }

        int start = 5;

        switch (puzzle){
            case 1:
                start = 0;
                break;
            case 2:
                start = 12;
                break;
            case 3:
                start = 0;
                break;
            case 4:
                start = 12;
                break;
            case 5:
                start = 9;

        }

        int finalStart = start;
        boolean returnValue = IntStream.range(BOARD_START_INDEX, PUZZLE_LENGTH-1)
                .allMatch(index ->checkConstraintColuwn(constraintValue, row, finalStart +index,index,val));

        for(int i =0;i<constraintValue.length;i++){
            if(constraintValue[i]>1){
                return false;
            }
        }

        return true;
    }

    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;
        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(_board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    private boolean subsectionConstraint2(int row, int column,int val) {
        int[] constraintValue = initConstraintValue();
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;
        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;
        int index = 0;
        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                checkConstraintRowAndColumn(constraintValue,r,c,index,val);
                index++;
            }
        }

        for(int i =0;i<constraintValue.length;i++){
            if(constraintValue[i]>1){
                return false;
            }
        }

        return true;

    }

    boolean checkConstraint2(
            int[][] board,
            int row,
            boolean[] constraint,
            int[] constraintValue,
            int column,int val, boolean isRow) {
        if(val!=-1 && val!=0 && _board[row][column]==val && isRow){
            constraintValue[row]++;
        }

        if(val!=-1 && val!=0 &&  _board[row][column]==val && !isRow){
            constraintValue[column]++;
        }

        return true;



    }

    boolean checkConstraintColuwn(
            int[] constraintValue,
            int row,int column,int index,int val) {
        if(val!=-1 && val!=0 && _board[row][column]==val ){
            constraintValue[val-1]++;
        }


        return true;


    }

    boolean checkConstraintRowAndColumn(
            int[] constraintValue,
            int row,int column,int index,int val) {
        if(val!=-1 && val!=0 && _board[row][column]==val ){
            constraintValue[val-1]++;
        }
        return true;


    }

    boolean checkConstraintRow(
            int[] constraintValue,
            int row,int column,int index,int val) {
        if(val!=-1 && val!=0 && _board[row][column]==val ){
            constraintValue[val-1]++;
        }
        return true;
    }

    boolean checkConstraint(
            int[][] board,
            int row,
            boolean[] constraint,
            int column) {
        if (board[row][column] == NO_VALUE) {
            return true;
        }
        if (board[row][column] != NO_VALUE) {
            if(board[row][column]>=MIN_VALUE && board[row][column]<=MAX_VALUE){
                if (!constraint[column - 1]) {
                    constraint[column - 1] = true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public int[] initConstraintValue() {
        int[] constraintValue = new int[PUZZLE_LENGTH];
        for (int row = 0; row < PUZZLE_LENGTH; row++) {
            constraintValue[row] = 0;
        }
        return constraintValue;
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