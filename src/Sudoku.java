import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

public class Sudoku extends JFrame {

    //=================================================================== fields
    private SudokuModel _sudokuLogic;//new SudokuModel(INITIAL_BOARD);
    private SudokuBoardDisplay _sudokuBoard;//new SudokuBoardDisplay(_sudokuLogic);

    private JTextField _rowTF = new JTextField(2);
    private JTextField _colTF = new JTextField(2);
    private JTextField _valTF = new JTextField(2);

    //============================================================== constructor
    public Sudoku() throws IOException {
        FileReader f = new FileReader("harita2.txt");
        BufferedReader in = new BufferedReader(f);
        String line,text="";

        line = in.readLine();
        int i=0;
        while (line!=null){
            i++;
            text += line;
            //System.out.println(i+". satır: "+text);;
            line = in.readLine();
        }
        f.close();

        _sudokuLogic = new SudokuModel(text);
        //_sudokuLogic.solve();
        _sudokuBoard = new SudokuBoardDisplay(_sudokuLogic);


        //System.out.println("Dosya Kapandı!!");
        // 1... Create/initialize components
        JButton moveBtn = new JButton("Ekle");
        JButton solveBtn = new JButton("Solve");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(new JLabel("Row (1-9):"));
        controlPanel.add(_rowTF);
        controlPanel.add(new JLabel("Col (1-9):"));
        controlPanel.add(_colTF);
        controlPanel.add(new JLabel("Val:"));
        controlPanel.add(_valTF);
        controlPanel.add(moveBtn);
        controlPanel.add(solveBtn);

        //... Add listener
        moveBtn.addActionListener(new MoveListener());
        solveBtn.addActionListener(new SolveMoveListener());
        // 2... Create content panel, set layout
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        // 3... Add the components to the content panel.
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(_sudokuBoard, BorderLayout.CENTER);

        // 4... Set this window's attributes, and pack it.
        setContentPane(content);
        setTitle("Sudoku 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);               // Don't let user resize it.
        pack();
        setLocationRelativeTo(null);       // Center it.
    }


    ///////////////////////////////////////////////////////////// MoveListener
    class MoveListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            try {
                //... Translate row and col to zero-based indexes.
                int row = Integer.parseInt(_rowTF.getText().trim()) - 1;
                int col = Integer.parseInt(_colTF.getText().trim()) - 1;
                int val = Integer.parseInt(_valTF.getText().trim());
                if (_sudokuLogic.isLegalMove(row, col, val)) {
                    _sudokuLogic.setVal(row, col, val);
                    _sudokuBoard.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Illegal row, col, or value.");
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter numeric values.");
            }
        }
    }

    ///////////////////////////////////////////////////////////// MoveListener
    class SolveMoveListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            try {
                //... Translate row and col to zero-based indexes.

                for (int row = 0; row < SudokuModel.BOARD_SIZE; row++) {
                    for (int col = SudokuModel.BOARD_START_INDEX; col < SudokuModel.BOARD_SIZE; col++) {
                        //if(_sudokuLogic.getVal(row,col)==SudokuModel.NO_VALUE){
                            for (int k = SudokuModel.MIN_VALUE; k <= SudokuModel.MAX_VALUE; k++) {
                                if(_sudokuLogic.getVal(row,col)==SudokuModel.NO_VALUE){
                                    _sudokuLogic.setVal(row, col, k);
                                }
                                if(_sudokuLogic.isLegalMove2(row, col,_sudokuLogic.getVal(row,col))) {
                                    _sudokuBoard.repaint();
                                }
                                else{
                                    _sudokuLogic.setVal(row, col, SudokuModel.NO_VALUE);
                                }
                            }
                        //}
                    }
                }

                for (int row = 0; row < SudokuModel.BOARD_SIZE; row++) {
                    for (int col = SudokuModel.BOARD_START_INDEX; col < SudokuModel.BOARD_SIZE; col++) {
                        if(_sudokuLogic.getVal(row,col)==SudokuModel.NO_VALUE){
                            int randInt = (new Random(System.currentTimeMillis())).nextInt(8)+1;
                            _sudokuLogic.setVal(row,col,randInt);
                            _sudokuBoard.repaint();
                        }
                    }
                }

            }catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter numeric values.");
            }
        }
    }

    //===================================================================== main
    public static void main(String[] args) throws IOException {
        new Sudoku().setVisible(true);
    }
}