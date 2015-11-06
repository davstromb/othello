package gui;


public class GameBoard {

    private DISK[][] xBoard = new DISK[8][8];
    private PlayerX player1 = new PlayerX(DISK.BLACK);
    private PlayerX player2 = new PlayerX(DISK.WHITE);
    private PlayerX currentPlayerX = player1;

    public GameBoard() {
        NewGame();
    }

    private void NewGame() {
        // create a new gameBoard  , all positions removed , score restart ;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                xBoard[i][j] = DISK.EMPTY;

            }
        }

        placeDisk(3, 3);
        nextTurn();
        placeDisk(3, 4);
        nextTurn();
        placeDisk(4, 4);
        nextTurn();
        placeDisk(4, 3);
        nextTurn();
    }






    public void placeDisk(int row, int col) {

        if (currentPlayerX.getColor() == DISK.BLACK) {
            xBoard[row][col] = DISK.BLACK;
        } else if (currentPlayerX.getColor() == DISK.WHITE) {
            xBoard[row][col] = DISK.WHITE;
        }
    }

    public DISK[][] getBoard() {
        return xBoard;
    }

    public void nextTurn() {
        currentPlayerX = (currentPlayerX == player1) ? player2 : player1;
    }

    public PlayerX getCurrentPlayerX() {
        return currentPlayerX;
    }

//    public boolean chkSlot(int currentRow, int currentCol) {
//        boolean flippable = false;
//
//        for (int chkRow = -1; chkRow < 2; chkRow++) {
//            for (int chkCol = -1; chkCol < 2; chkCol++) {
//                // Explore all straight and diagonal directions from the piece put down.
//                // Move along that direction - if there is at least one piece of the opposite color next
//                // in line, and the pieces of the opposite color are followed by a piece of the same
//                // color, do a flip.
//                if (chkRow == 0 && chkCol == 0) {
//                    continue;
//                }
//
//                int xRow = currentRow + chkRow;
//                int xCol = currentCol + chkCol;
//
//                if (xRow >= 0 && xRow <= 7 && xCol >= 0 && xCol <= 7) {
//                    if ((xBoard[xRow][xCol]) == (this.currentPlayerX.getColor() == DISK.BLACK ? DISK.WHITE : DISK.BLACK)) {
//                        for (int range = 0; range < 8; range++) {
//                            int nRow = currentRow + range * chkRow;
//                            int nCol = currentCol + range * chkCol;
//                            if (nRow < 0 || nRow > 7 || nCol < 0 || nCol > 7) {
//                                continue;
//                            }
//
//                            if (xBoard[nRow][nCol] == this.currentPlayerX.getColor()) {
//                                flippable = true;
//                                break;
//                            }
//
//                        }
//
//                    }
//                }
//
//
//            }
//        }
//        return flippable;
//    }

    public boolean doFlip(int currentRow, int currentCol, boolean flippable) {

        System.out.println(currentRow + " " + currentCol + " " + flippable);

        boolean isValid = false;
        for (int chkRow = -1; chkRow < 2; chkRow++) {
            for (int chkCol = -1; chkCol < 2; chkCol++) {
                if (chkRow == 0 && chkCol == 0) {
                    continue;
                }

                int xRow = currentRow + chkRow;
                int xCol = currentCol + chkCol;

                if (xRow >= 0 && xRow <= 7 && xCol >= 0 && xCol <= 7) {
                    if ((xBoard[xRow][xCol]) == (this.currentPlayerX.getColor() == DISK.BLACK ? DISK.WHITE : DISK.BLACK)) {
                        for (int range = 0; range < 8; range++) {

                            int nRow = currentRow + range * chkRow;
                            int nCol = currentCol + range * chkCol;
                            if (nRow < 0 || nRow > 7 || nCol < 0 || nCol > 7) {
                                continue;
                            }

                            if (xBoard[nRow][nCol] == this.currentPlayerX.getColor()) {
                                if (flippable) {
                                    for (int flipDistance = 1; flipDistance < range; flipDistance++) {
                                        int finalRow = currentRow + flipDistance * chkRow;
                                        int finalCol = currentCol + flipDistance * chkCol;

                                        xBoard[finalRow][finalCol] = this.currentPlayerX.getColor();
                                    }
                                }
                                isValid = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return isValid;
    }


    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();



        return sb.toString();
    }

    public int chkWinner() {
        int slotsLeft = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (xBoard[i][j] == DISK.EMPTY) {
                    slotsLeft++;
                }


            }
        }
        return slotsLeft;
    }
}
