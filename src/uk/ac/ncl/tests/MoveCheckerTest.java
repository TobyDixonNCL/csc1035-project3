package uk.ac.ncl.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.entity.Cell;
import uk.ac.ncl.entity.CellStatus;
import uk.ac.ncl.game.MoveChecker;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static uk.ac.ncl.Constants.BOARD_SIZE;

class MoveCheckerTest {

    private MoveChecker moveChecker;
    private Cell[][] cells;

    @BeforeEach
    void setUp() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        moveChecker = new MoveChecker(cells);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                CellStatus cellStatus = CellStatus.EMPTY;
                Cell tempEl = new Cell(cellStatus, new JButton(), i, j);
                if ((i == 3 && j == 4) || (i == 4 && j == 3)) {
                    tempEl.setValue( CellStatus.LIGHT);
                } else if ((i == 4 && j == 4) || (i == 3 && j == 3)) {
                    tempEl.setValue( CellStatus.DARK);
                }
                cells[i][j] = tempEl;
            }
        }
    }

    @Test
    void generateOpponentDark() {
        Cell cell = moveChecker.generateOpponent(CellStatus.DARK);

        assertTrue(cells[2][4] == cell || cells[3][5] == cell ||
                cells[4][2] == cell || cells[4][3] == cell);
        assertTrue(cell.getMove() != null);
        assertTrue(cell.getMove().getMoves().size() == 1);
        assertTrue(cell.getMove().getScore() == 1);
    }

    @Test
    void generateOpponentLight() {
        Cell cell = moveChecker.generateOpponent(CellStatus.LIGHT);

        assertTrue(cells[2][3] == cell || cells[3][2] == cell ||
                cells[4][5] == cell || cells[5][4] == cell);
        assertTrue(cell.getMove() != null);
        assertTrue(cell.getMove().getMoves().size() == 1);
        assertTrue(cell.getMove().getScore() == 1);
    }

    @Test
    void flipCheckers() {
        cells[5][3].isLegal(CellStatus.DARK, this.cells);
        moveChecker.flipPieces(cells[5][3], CellStatus.DARK);
        assertTrue(cells[4][3].getValue() == CellStatus.LIGHT);
        assertTrue(cells[4][3].getMove() == null);
    }

    @Test
    void findPotentialMoves() {
        ArrayList<Cell> grayCells = moveChecker.findPotentialMoves(CellStatus.DARK);
        assertTrue(grayCells.size() == 4);
        assertTrue(grayCells.contains(cells[2][4]));
        assertTrue(grayCells.contains(cells[3][5]));
        assertTrue(grayCells.contains(cells[4][2]));
        assertTrue(grayCells.contains(cells[5][3]));
    }

    @Test
    void colourPieces() {
        CellStatus testColour = CellStatus.GRAY;
        ArrayList<Cell> piecesToColour = new ArrayList<Cell>();
        piecesToColour.add(cells[2][4]);
        piecesToColour.add(cells[3][5]);
        piecesToColour.add(cells[4][2]);
        piecesToColour.add(cells[5][3]);
        moveChecker.colourPieces(piecesToColour, CellStatus.GRAY);
        assertTrue(cells[2][4].getValue() == testColour);
        assertTrue(cells[3][5].getValue() == testColour);
        assertTrue(cells[4][2].getValue() == testColour);
        assertTrue(cells[5][3].getValue() == testColour);
    }

    @Test
    void getFinalScore() {
        String result = "The game is over. It is a draw. Each player has " + 2 + " pieces";
        assertTrue(result.equals(moveChecker.getFinalScore()));
    }

    @Test
    void removeMoves() {
        ArrayList<Cell> grayCells = moveChecker.findPotentialMoves(CellStatus.DARK);
        assertTrue(cells[2][4].getMove() != null);
        assertTrue(cells[3][5].getMove() != null);
        assertTrue(cells[4][2].getMove() != null);
        assertTrue(cells[5][3].getMove() != null);
        moveChecker.removeMoves(grayCells);
        assertTrue(cells[2][4].getMove() == null);
        assertTrue(cells[3][5].getMove() == null);
        assertTrue(cells[4][2].getMove() == null);
        assertTrue(cells[5][3].getMove() == null);
    }

    @Test
    void testIsLegal(){
        assertTrue(cells[5][3].isLegal(CellStatus.DARK, this.cells));
        assertTrue(cells[5][3].getMove().getMoves().size() == 1);
        assertTrue(cells[5][3].getMove().getMoves().get(0).getCell().getColumn() == 3);
        assertTrue(cells[5][3].getMove().getMoves().get(0).getCell().getRow() == 3);
        assertTrue(cells[5][3].getMove().getScore() == 1);
    }
}