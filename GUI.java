import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*; 
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import com.google.common.collect.Lists;

//Aida Maisarah Hisam, Sharleen Ravi Mahendra, Farah Kamila Yahya
public class GUI {  

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board talabiaBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece playerMovePiece;
    private BoardDirection boardDirection;
    private final static Dimension OuterFrame = new Dimension(600, 600);
    private final static Dimension BoardFrame = new Dimension(400, 350);
    private final static Dimension TileFrame = new Dimension(10, 10);
    private final static Color lightTileColor = Color.GRAY;
    private final static Color darkTileColor = Color.WHITE;
    private static String defaultPath = "image/";

    public GUI() {
        this.gameFrame = new JFrame("Talabia Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenu = displayMenuBar();
        this.gameFrame.setJMenuBar(tableMenu);
        this.gameFrame.setSize(OuterFrame);
        this.talabiaBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true); 
    }

    //AIDA MAISARAH
    private JMenuBar displayMenuBar() {
        final JMenuBar tableMenu = new JMenuBar();
        tableMenu.add(createFileMenu());
        return tableMenu;
    }
 
    //AIDA MAISARAH
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openFile = new JMenuItem("Loading the file");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening the file");
            }
        });
        fileMenu.add(openFile);
 
        final JMenuItem exitMenuItem = new JMenuItem("Closing the application");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    
    //FARAH KAMILA YAHYA
    //an enum because board direction has only two directions, NORMAL and when the board is FLIPPED
    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }
            @Override   
            BoardDirection opposite(){ //when opposite, the NORMAL becomes FLIPPED
                return FLIPPED; 
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite() { //when opposite, the FLIPPED becomes the NORMAL
                return NORMAL; 
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    //AIDA MAISARAH HISAM
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(6, 7));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUMTILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BoardFrame);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    //JButton for the tiles
    //SHARLEEN RAVI MAHENDRA
    public class TilePanel extends JButton {
        private final int tileID;

        public TilePanel(final BoardPanel boardPanel, final int tileID) {
            this.tileID = tileID;
            setPreferredSize(TileFrame);
            setTileColor();
            assignPieceOnTile(talabiaBoard);

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sourceTile == null) {
                        sourceTile = talabiaBoard.getTile(TilePanel.this.tileID);
                        playerMovePiece = sourceTile.getPiece();
                        if (playerMovePiece == null) {
                            sourceTile = null;
                        }
                    } else {
                        destinationTile = talabiaBoard.getTile(TilePanel.this.tileID);
                        final Move move = Move.MoveFactory.createMove(talabiaBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                        final MoveTransition transition = talabiaBoard.currentPlayer().makeMove(move);
                        if (transition.getMoveStatus().isDone()) {
                            talabiaBoard = transition.getTransitionBoard();
                            sourceTile.getPiece().movePiece(move); // Update the piece's position
                            sourceTile = null;
                            destinationTile = null;
                            playerMovePiece = null;
                            boardPanel.drawBoard(talabiaBoard);
                            flipBoard();
                        }
                    }
                }
            });
            validate();
        }
        
        //FARAH KAMILA YAHYA
        public void flipBoard(){
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(talabiaBoard);
        }

        //SHARLEEN RAVI MAHENDRA
        public void drawTile(final Board board) {
            setTileColor();
            assignPieceOnTile(board);
            validate();
            repaint();
        }

        //making the checkered tiles
        //AIDA MAISARAH HISAM
        private void setTileColor() {
            if (BoardUtils.FIRSTRANK[this.tileID] || BoardUtils.SECONDRANK[this.tileID] || BoardUtils.THIRDRANK[this.tileID] ||
                    BoardUtils.FOURTHRANK[this.tileID] || BoardUtils.FIFTHRANK[this.tileID] || BoardUtils.SIXTHRANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
            }
        }

        //assigning pieces on tiles
        //AIDA MAISARAH HISAM
        private void assignPieceOnTile(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileID).isTileOccupied()) {
                 try {
                     final BufferedImage originalImage = ImageIO.read(new File(defaultPath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1) +
                                                                       board.getTile(this.tileID).getPiece().toString() + ".PNG"));

                     //rotate the original image based on the board direction
                     int rotationAngle = (boardDirection == BoardDirection.FLIPPED) ? 180 : 0;
                     Image rotatedImage = rotateImage(originalImage, rotationAngle);

                     //scale the image 
                     final int width = TileFrame.width * 6;
                     final int height = TileFrame.height * 6;
                     Image scaledImage = rotatedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                     ImageIcon rotatedIcon = new ImageIcon(scaledImage);
                     this.setIcon(rotatedIcon);
                     } catch (IOException e) {
                          e.printStackTrace();
                  }
            } else {
                   this.setIcon(null);
            }
        }

        private Image rotateImage(final BufferedImage originalImage, int degrees) {
           double rotationRequired = Math.toRadians(degrees);
           double locationX = originalImage.getWidth() / 2.0;
           double locationY = originalImage.getHeight() / 2.0;
           AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
           AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
           return op.filter(originalImage, null);
        }
    }
}