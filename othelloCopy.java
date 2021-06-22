import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/* print ArrayList in java
System.out.println(Arrays.deepToString(list.toArray()));
 */

public class othelloCopy {
    private int boardSize;
    private String[][] board;
    private char human;
    private char robot;
    private char turn;
    private char winner;
    private String white;
    private String black;
    Scanner in = new Scanner(System.in);

    // constructor
    public othelloCopy(int m){
        boardSize = m;
        board = new String[m][m];
        human = 'W';
        robot = 'B';
        turn = 'W';
        winner = ' ';
        white = "X";
        black = "O";
//        white = "⚪";
//        black = "⚫";
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = " ";
            }
        }
        board[3][3] = white;
        board[4][4] = white;
        board[3][4] = black;
        board[4][3] = black;

    }
    // check if given (x, y) is on board
    public boolean on_board(int x, int y){
        return (x>= 0 && x <= 7) && (y >= 0 && y <= 7);
    }

    // return an arrayList of tiles_to_flip
    public ArrayList<ArrayList<Integer>> tiles_to_flip(int xStart, int yStart){
        String myTile;
        String yourTile;
        ArrayList<ArrayList<Integer>> tiles_to_flip = new ArrayList<>();
        int [][] direction = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

        if (turn == 'W'){
            myTile = white;
            yourTile = black;
        }
        else{
            myTile = black;
            yourTile = white;
        }
        for (int i = 0; i < 8; i++){
            int vertical = direction[i][0];
            int horizontal = direction[i][1];
            int x = xStart, y = yStart;
            x += vertical;
            y += horizontal;

            while(on_board(x, y) && board[x][y] == yourTile){
                x += vertical;
                y += horizontal;
                if (on_board(x, y) && board[x][y] == myTile){
                    while (true){
                        x -= vertical;
                        y -= horizontal;
                        if (x == xStart && y == yStart){
                            break;
                        }
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(x);
                        temp.add(y);
                        tiles_to_flip.add(temp);
                    }
                }
            }

        }
        return tiles_to_flip;
    }

    // check if the (x, y) is a valid move
    public boolean check_valid_moves(int xStart, int yStart){
        if (!on_board(xStart, yStart) || board[xStart][yStart] != " "){
            return false;
        }
        int size = tiles_to_flip(xStart, yStart).size();
        // if the length of tiles_to_flip == 0 return false
        if (size == 0)
            return false;
        return true;
    }

    // return an array of valid_moves
    public ArrayList<ArrayList<Integer>> get_valid_moves(){
        ArrayList<ArrayList<Integer>> validMoves= new ArrayList<>();
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if (check_valid_moves(i, j)){
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(i);
                    temp.add(j);
                    validMoves.add(temp);
                }
            }
        }
        return validMoves;
    }

    public boolean flip_tiles(int xStart, int yStart){
        ArrayList<ArrayList<Integer>> flip_tiles = new ArrayList<>();
        String myTile;
        if (!check_valid_moves(xStart, yStart))
            return false;
        flip_tiles = tiles_to_flip(xStart, yStart);
        if (turn == 'W'){
            myTile =white;
        }
        else {
            myTile = black;
        }
        board[xStart][yStart] = myTile;
        for(int i = 0; i < flip_tiles.size(); i++){
            int x = flip_tiles.get(i).get(0);
            int y = flip_tiles.get(i).get(1);
            board[x][y] = myTile;
        }
        return true;
    }
    public ArrayList<Integer> get_input(){
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        moves = get_valid_moves();
        System.out.print("Please enter you choice: " + moves.toString() + "\n[vertical, horizontal]\n" +
                "Enter with a space and no comma (ex: 0 0 ): ");
        String input = in.nextLine();
        String [] input_str = input.split(" ");
        ArrayList<Integer> input_list = new ArrayList<>();
        for(int i = 0; i < input_str.length; i++){
            int num = Integer.parseInt(input_str[i]);
            input_list.add(num);
        }
        return input_list;
    }

    public void move(){
        ArrayList<Integer> next = new ArrayList<>();
        if ((turn == 'W' && human == 'W') || (turn == 'B' && human == 'B')){
            next = get_input();
        }
        else if((turn == 'W' && robot == 'W') || (turn == 'B' && robot == 'B')){
            System.out.println("Press Enter to see the computer's move.");
            in.nextLine();
            System.out.println("Computer's move");
            next = get_random();
        }
        int i = next.get(0);
        int j = next.get(1);
        System.out.println(Arrays.deepToString(next.toArray()));
        flip_tiles(i, j);
        if (game_over()){
            return;
        }
        toggle();
    }

    public ArrayList<Integer> get_random(){
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        moves = get_valid_moves();
        ArrayList<Integer> random_choice = new ArrayList<>();
        ArrayList<Integer> corner_choice = new ArrayList<>();
        Random rand = new Random();
        int rand_num = rand.nextInt(moves.size());
        moves = get_valid_moves();
        for(int i = 0; i < moves.size(); i++){
            int x = moves.get(i).get(0);
            int y = moves.get(i).get(1);
            if (get_corner(x, y)){
                corner_choice.add(x);
                corner_choice.add(y);
                return corner_choice;
            }
        }
        //System.out.println(Arrays.deepToString(moves.toArray()));
        random_choice = moves.get(rand_num);
        return random_choice;
        //
    }
    public boolean get_corner(int x, int y){
        return (x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) ||
                (x == 7 && y == 7);
    }
    public boolean game_over(){
        int count_white = 0;
        int count_black = 0;
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] == white)
                    count_white++;
                else if (board[i][j] == black)
                    count_black++;
            }
        }
        if (count_white > count_black && count_white + count_black == 64){
            winner = 'X';
            return true;
        }
        else if (count_white < count_black && count_white + count_black == 64){
            winner = 'O';
            return true;
        }
        else if (count_white == count_black && count_white + count_black == 64){
            winner = 'T';
            return true;
        }
        return false;
    }
    public char get_winner(){
        return winner;
    }
    public void toggle() {
        if (turn == 'W')
            turn = 'B';
        else
            turn = 'W';
    }
    public void white_or_black(){
        System.out.print("Choose White or Black\n" +
                "White will go first and Black will go next\n" +
                "Enter \"W\" for White and \"B\" for Black: ");
        String user_input = in.nextLine();
        char input = user_input.charAt(0);
        while (input != 'W' && input != 'B'){
            System.out.println("Error! Enter \"W\" for White and \"B\" for Black: ");
            user_input = in.nextLine();
            input = user_input.charAt(0);
        }
        if (input == 'W'){
            System.out.println("You will be White, you will make the move first");
            human = 'W';
            robot = 'B';
        }
        else {
            System.out.println("You will be Black, computer will make the move first");
            human = 'B';
            robot = 'W';
        }
    }

    public String[][] getBoard(){
        return board;
    }

    public boolean humansTurn(int i, int j){

        if (turn != human)
            return false;

        if (check_valid_moves(i, j)){
            System.out.println("yes");
            flip_tiles(i, j);
            toggle();
            return true;
        }
        return false;
    }

    public char getTurn(){
        return turn;
    }


    public void printBoard(){
        for(int i = 0; i < boardSize;i++){
            System.out.print(i);
            System.out.print("\t");
            for(int j = 0; j < boardSize; j++){

                System.out.print(board[i][j]);
                System.out.print("\t");
            }
            System.out.println();
            System.out.println();
        }
        System.out.print("\t");
        for(int i = 0; i < boardSize; i++){
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
        System.out.println();
    }




}