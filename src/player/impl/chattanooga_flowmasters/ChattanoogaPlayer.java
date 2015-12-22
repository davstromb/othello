package player.impl.chattanooga_flowmasters;

import game.COLOR;
import game.Position;
import player.Board;
import player.Player;

import java.util.*;

public class ChattanoogaPlayer extends Player {

    private final double K2 = 1;
    private final double K3 = 1;

    private final Comparator<ImmutablePair<Position, Double>> immutablePairComparator = (ip1, ip2) -> -ip1.getRight().compareTo(ip2.getRight());

    private int[][] scoreMatrix =
                    {{ 30, -25, 10, 5, 5, 10, -25,  30,},
                    {-25, -25,  1, 1, 1,  1, -25, -25},
                    { 10,   1,  5, 2, 2,  5,   1,  10},
                    {  5,   1,  2, 1, 1,  2,   1,   5},
                    {  5,   1,  2, 1, 1,  2,   1,   5},
                    { 10,   1,  5, 2, 2,  5,   1,  10},
                    {-25, -25,  1, 1, 1,  1, -25, -25},
                    { 30, -25, 10, 5, 5, 10, -25,  30}};

    public ChattanoogaPlayer(COLOR color) {
        super(color);
    }

    @Override
    public void newGame() {

    }

    @Override
    public Position nextMove() throws InterruptedException {

        List<ImmutablePair<Position, Double>> positionScores = new ArrayList<>(availablePositions.size());



        for (Position availablePosition : availablePositions) {
            positionScores.add(new ImmutablePair<>(availablePosition, scorePosition(availablePosition, currentBoard)));
        }



        Collections.sort(positionScores, immutablePairComparator);
        printMove(positionScores);
        return positionScores.get(0).getLeft();
    }

    private void printMove(List<ImmutablePair<Position, Double>> positionScores) {
        System.out.println("***");
        for(ImmutablePair immutablePair : positionScores) {
            System.out.println("Position: " + immutablePair.getLeft() + " Score: " + immutablePair.getRight());
        }
    }


    private double scorePosition(final Position position, final Board board) {
        ImmutablePair<Integer, Integer> stonesCount = calculateNumberOfStonesAfterMove(position, board);
        int friendlyStones = 0;
        int enemyStones = 0;
        double K1 = 1;
        if (this.COLOR == game.COLOR.BLACK) {
            K1 = 0.5;
            friendlyStones = stonesCount.getLeft();
            enemyStones = stonesCount.getRight();
        } else {
            K1 = 0.1;
            enemyStones = stonesCount.getLeft();
            friendlyStones = stonesCount.getRight();
        }

        int diff = friendlyStones - enemyStones;
        int totalStones = friendlyStones + enemyStones;

        boolean isEarlyGame = totalStones < 40;
        double countGoodness = 0;

        if (isEarlyGame) {
            countGoodness = K1 * -diff;
        } else {
            countGoodness = K2 * diff;
        }

        double positionalGoodness = K3 * countGoodness(position);

        return positionalGoodness + countGoodness;
    }

    private int countGoodness(Position position) {
        return scoreMatrix[position.column][position.row];
    }

    /**
     *
     * @param position the position where the move is placed
     * @param board the board state before placing the stone
     * @return An {@link player.impl.chattanooga_flowmasters.ChattanoogaPlayer.ImmutablePair} with LEFT being blacks stones and RIGHT being whites stones.
     */
    private ImmutablePair<Integer, Integer> calculateNumberOfStonesAfterMove(final Position position, final Board board) {
        Board copyBoard = board.copy();
        copyBoard.placeDisk(this.COLOR, position);
        return getStoneCount(copyBoard);
    }

    /**
     *
     * @param board - the current board
     * @return - An {@link player.impl.chattanooga_flowmasters.ChattanoogaPlayer.ImmutablePair} with LEFT being blacks stones and RIGHT being whites stones.
     */
    private ImmutablePair<Integer, Integer> getStoneCount(Board board) {
        Integer blackCount = 0;
        Integer whiteCount = 0;
        COLOR[][] colorMatrix = board.getColorMatrix();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (colorMatrix[i][j] == game.COLOR.WHITE) {
                    whiteCount++;
                } else if (colorMatrix[i][j] == game.COLOR.BLACK) {
                    blackCount++;
                }
            }
        }
        return new ImmutablePair<>(blackCount, whiteCount);
    }

    private class ImmutablePair<LEFT, RIGHT> {
        private final LEFT left;
        private final RIGHT right;


        private ImmutablePair(LEFT left, RIGHT right) {
            this.left = left;
            this.right = right;
        }


        LEFT getLeft() {
            return left;
        }

        RIGHT getRight() {
            return right;
        }
    }


}