import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//Farah Kamila Yahya, Aida Maisarah Hisam
public class Talabia extends JPanel {  
    private Image backgroundImage; 

    public Talabia() {
        //AIDA (setting the background image)
        try {
            backgroundImage = ImageIO.read(new File("image/a.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0)); // for a transparent bg

        //FARAH 
        JLabel gameLabel = new JLabel("Talabia Chess");
        gameLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameLabel.setForeground(Color.decode("#dac9f7"));

        JLabel groupLabel = new JLabel("by Group 17");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 18));
        groupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        groupLabel.setForeground(Color.decode("#dac9f7"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton startButton = new JButton("START GAME");
        startButton.setFocusable(false);
        Dimension buttonSize = new Dimension(200, 50);
        startButton.setPreferredSize(buttonSize);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Board board = Board.createStandardBoard();
                        System.out.println(board);

                        GUI gui = new GUI();
                        JFrame menuFrame = (JFrame) SwingUtilities.getWindowAncestor(Talabia.this);
                        menuFrame.dispose();
                    }
                });
            }
        });

        buttonPanel.add(startButton);

        add(gameLabel, BorderLayout.NORTH);
        add(groupLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //the main method to run the application
    public static void main(String[] args) {  
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame menuFrame = new JFrame("Talabia Chess");
                menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.getContentPane().add(new Talabia());
                menuFrame.setSize(600, 600);
                menuFrame.setLocationRelativeTo(null);
                menuFrame.setVisible(true);
            }
        });
    }
}