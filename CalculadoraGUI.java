import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculadoraGUI extends JFrame {
    private JTextField display;
    private Calculadora calculadora;

    private double valor1 = 0;
    private String operacao = "";
    private boolean novoNumero = true;

    public CalculadoraGUI() {
        calculadora = new Calculadora();
        criarInterface();
    }

    private void criarInterface() {
        setTitle("Calculadora Simples");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Campo de texto no topo
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Painel para botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(5, 4, 5, 5));

        String[] botoes = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        for (String texto : botoes) {
            JButton botao = new JButton(texto);
            botao.setFont(new Font("Arial", Font.BOLD, 18));
            botao.addActionListener(new BotaoListener());
            painelBotoes.add(botao);
        }

        add(painelBotoes, BorderLayout.CENTER);
    }

    private class BotaoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String comando = e.getActionCommand();

            if ("0123456789".contains(comando)) {
                if (novoNumero) {
                    display.setText(comando);
                    novoNumero = false;
                } else {
                    display.setText(display.getText() + comando);
                }
            } else if (comando.equals("C")) {
                valor1 = 0;
                operacao = "";
                display.setText("0");
                novoNumero = true;
            } else if (comando.equals("=")) {
                calcularResultado();
            } else {  // Operações + - * /
                if (!operacao.isEmpty()) {
                    calcularResultado();
                }
                try {
                    valor1 = Double.parseDouble(display.getText());
                } catch (NumberFormatException ex) {
                    display.setText("Erro");
                    return;
                }
                operacao = comando;
                novoNumero = true;
            }
        }
    }

    private void calcularResultado() {
        if (operacao.isEmpty() || novoNumero) {
            return; // nada para calcular
        }

        double valor2;
        try {
            valor2 = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            display.setText("Erro");
            return;
        }

        double resultado = 0;
        try {
            switch (operacao) {
                case "+":
                    resultado = calculadora.somar(valor1, valor2);
                    break;
                case "-":
                    resultado = calculadora.subtrair(valor1, valor2);
                    break;
                case "*":
                    resultado = calculadora.multiplicar(valor1, valor2);
                    break;
                case "/":
                    resultado = calculadora.dividir(valor1, valor2);
                    break;
            }
            display.setText(String.valueOf(resultado));
            valor1 = resultado;
            novoNumero = true;
            operacao = "";
        } catch (ArithmeticException ex) {
            display.setText("Erro: " + ex.getMessage());
            valor1 = 0;
            operacao = "";
            novoNumero = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculadoraGUI().setVisible(true);
        });
    }
}
