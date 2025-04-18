//This JAVA app creates a Graphical User Interface (GUI) Calculator using the Swing library. It supports,
//  Basic math operations : + , - , *, /
//  Real-time output
//  "=" to calculate result
//  "C" to clear everything

//import Swing, Java's GUI toolkit to create windows , buttons, text fields, etc.
import javax.swing.*;

//Main class and Method
// Entry point of the JAVA program
// The app starts execution from here
public class Main {
    public static void main(String[] args) {

        // Create the Jframe (Main Window)
           // JFrame is the main window
           // setLayout(null) = absolute positioning
        JFrame frame = new JFrame("Calculator");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create the display field
          // A JTextField is used to display numbers and results.
          // setBounds(x, y, width , height) positions the text field manually.
        JTextField textField = new JTextField();
        textField.setBounds(30, 40, 330, 40);
        frame.add(textField);

        // Create calculator buttons
        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        //An array of JButton objects is created to hold the buttons
        JButton[] buttons = new JButton[16];


        //Position the Buttons :- Loops through each label to create a button
        //                     :- Positions them in a 4*4 grid
        int x = 30, y = 100;
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBounds(x, y, 70, 50);
            frame.add(buttons[i]);
            x += 80;
            if ((i + 1) % 4 == 0) {
                x = 30;
                y += 60;
            }
        }

        // Create a clear button
        JButton clearButton = new JButton("C");
        clearButton.setBounds(30, y, 330, 50);
        frame.add(clearButton);

        //Handle button actions
        StringBuilder input = new StringBuilder();

        for (JButton button : buttons) {
            //Add action to each button
            // When any button is clicked, this code runs.
            // Gets the label of the clicked button.
            button.addActionListener(e -> {
                String cmd = ((JButton) e.getSource()).getText();
                if (cmd.equals("=")) {
                    try {
                        String result = String.valueOf(eval(input.toString()));
                        textField.setText(result);
                        input.setLength(0);
                        input.append(result);
                    } catch (Exception ex) {
                        textField.setText("Error");
                        input.setLength(0);
                    }
                } else {
                    input.append(cmd);
                    textField.setText(input.toString());
                }
            });
        }

        clearButton.addActionListener(e -> {
            input.setLength(0);
            textField.setText("");
        });

        frame.setVisible(true);
    }

    public static double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length())
                    throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
