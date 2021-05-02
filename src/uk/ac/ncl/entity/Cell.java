/**
 * @author Kostiantyn Potomkin
 * @version 0.9
 * @since 05-03-2020
 */
package uk.ac.ncl.entity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static uk.ac.ncl.Constants.BOARD_SIZE;

/**
 * Represents each cell of the playing board.
 */
public class Cell {

    /**
     * Current cell status.
     */
    private CellStatus value;
    /**
     * Links cell to the corresponding UI element.
     */
    private JButton jButton;
    /**
     * Cell's row
     */
    private int row;
    /**
     * Cell's column
     */
    private int column;

    /**
     * Potential moves of the piece
     */
    private Move move;

    public Cell(CellStatus value, JButton jButton, int row, int column) {
        this.value = value;
        this.jButton = jButton;
        this.row = column;
        this.column = row;
        this.move = null;
    }

    public CellStatus getValue() {
        return value;
    }

    /**
     * Changes button's design to have an effect of the "pressed" button
     */
    public void colourTemp(Color colour, boolean isPressed) {
        this.jButton.setBackground(colour);
        if (isPressed) {
            this.jButton.setBorderPainted(true);
            this.jButton.setBorder(new LineBorder(Color.RED));
        } else {
            this.jButton.setBorderPainted(false);
        }
    }

    /**
     * Updates the status of the cell
     */
    public void setValue(CellStatus value) {
        this.value = value;
        switch (value) {
            case EMPTY:
                this.jButton.setBackground(new Color(820000));
                break;
            case LIGHT:
                this.jButton.setBackground(Color.WHITE);
                break;
            case DARK:
                this.jButton.setBackground(Color.BLACK);
                break;
            case GRAY:
                this.jButton.setBackground(Color.GRAY);
                break;
            default:
                break;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    /**
     * Checks whether there exists a legal move for the piece.
     * If such a move exists, returns true and adds information to the piece.
     *
     * @param colour - colour of the current player
     * @param cells  - current information about the board
     * @return whether move is possible for the piece. If this is the case, then possible moves are stored in Piece.
     */
    public boolean isLegal(CellStatus colour, Cell[][] cells) {
        CellStatus opponent = colour == CellStatus.LIGHT ? CellStatus.DARK : CellStatus.LIGHT;
        boolean isLegal = false;
        int score = 0;
        ArrayList<DirectedMove> moves = new ArrayList<DirectedMove>();
        int[][] DIRS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {1, 1}, {1, 0}, {1, -1}};

        for (int[] dir : DIRS) {
            int temp_score = 0;
            int d_row = this.getRow() + dir[0];
            int d_col = this.getColumn();
            if (0 <= d_col && d_col < BOARD_SIZE && 0 <= d_row && d_row < BOARD_SIZE
                    && cells[d_row][d_col].getValue() != CellStatus.EMPTY
                    && cells[d_row][d_col].getValue() != opponent) {
                while (true) {
                    d_row += dir[0];
                    d_col += dir[1];
                    temp_score += 1;
                    if (0 <= d_col && d_col < BOARD_SIZE && 0 <= d_row && d_row < BOARD_SIZE
                            && cells[d_row][d_col].getValue() != CellStatus.EMPTY) {
                        if (cells[d_row][d_col].getValue() == colour) {
                            isLegal = true;
                            score = temp_score;
                            moves.add(new DirectedMove(cells[d_row][d_col], dir));
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        Move move = new Move(moves, score);
        this.setMove(move);
        return isLegal;
    }
}