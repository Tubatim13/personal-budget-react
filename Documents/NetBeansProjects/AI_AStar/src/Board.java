/**
 * Simple A-star/Heuristic Start to Finish mapping
 * @version 8-24-2021
 * @author Tim Hillmann <tubatim13@aol.com>
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class Board implements ActionListener{
    
    private JFrame frame = new JFrame();
    private JPanel board = new JPanel();
    private JButton[][] buttons = new JButton[15][15];
    private Node[][] nodes = new Node[15][15];
    private int press = 0;
    private static Node start = new Node(0,0,0);
    private static Node end = new Node(0,0,0);
    private static Node curr = new Node(0,0,0);
    private static Node prev = new Node(0,0,0);
    private PriorityQueue<Node> open = new PriorityQueue<Node>(new MyComparator());
    private ArrayList<Node> closed = new ArrayList<Node>();
    private ArrayList<Node> finPath = new ArrayList<Node>(); 
    private int numNew = 0;
    private static JPanel panel = new JPanel(new GridBagLayout());;
    
    public class MyComparator implements Comparator<Node>
    {
        @Override
        public int compare( Node x, Node y )
        {
            return x.getF() - y.getF();
        }
    }
    
    public Board(){}
    
    public static void main(String[] args) {
        Board board = new Board();
        board.createBoard();
        JOptionPane.showMessageDialog(null, "Please select an initial node and an ending\n" + 
                "node from one of the gray positions available.", "Info", JOptionPane.WARNING_MESSAGE);
        
    }
    
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        int positionX = 0;
        int positionY = 0;
        for(int i = 0; i < buttons.length; i++){
            for(int  j = 0; j < buttons[i].length; j++){
                if(buttons[i][j] == button){
                    positionX = j;
                    positionY = i;
                }
            }
        }
        if(press == 0){
            button.setBackground(Color.green);
            start = new Node(positionX,positionY,0);
            curr = new Node(positionX,positionY,0);
            press++;
            open.add(start);
        }else{
            button.setBackground(Color.red);
            end = new Node(positionX,positionY,0);
            for(JButton[] currButt : buttons){
                for(JButton curr : currButt){
                    curr.setEnabled(false);
                }
            }
            runTests();
        }
    }
    
    public void createBoard(){
        board.setLayout(new GridLayout(15,15));
        for(int i = 0; i < buttons.length;i++){
            for(int j = 0; j < buttons[i].length; j++){
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.lightGray);
                buttons[i][j].addActionListener(this);
                nodes[i][j] = new Node(i,j,0);
                board.add(buttons[i][j]);
            }
        }
        ArrayList<Integer> blacked = new ArrayList<Integer>();
        for(int i = 0; i <23; i++){
            int selected = (int)(Math.random()*225);
            while(blacked.contains(selected)){
                selected = (int)(Math.random()*225);
            }
            blacked.add(selected);
            buttons[selected%15][selected/15].setEnabled(false);
            buttons[selected%15][selected/15].setBackground(Color.black);
            nodes[selected%15][selected/15].setType(1);
            closed.add(nodes[selected/15][selected%15]);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(board);
        frame.setSize(700, 700);
        frame.show();
    }
    
    public int calculate(Node node){
        if(node.getParent() == null)
        {
            node.setG(0);
        }else{
            node.setG(node.getParent().getG()+1);
        }
        node.setH((int)(Math.sqrt(Math.pow((node.getCol()-end.getCol()),2)+Math.pow(node.getRow()-end.getRow(),2))));
        node.setF();
        return node.getF();
    }
    
    public ArrayList<Node> findNeighbors(){
        numNew++;
        ArrayList<Node> neighbors = new ArrayList<Node>();
        try{
            int y = curr.getRow();
            int x = curr.getCol();

            Node top;
            Node bottom;
            Node left;
            Node right;
            try{ top = nodes[x][y-1]; 
                if(!open.contains(top)&&!closed.contains(top)){top.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(top); open.add(top); neighbors.add(nodes[curr.getCol()][curr.getRow()+1]);}
                try{
                    int check = top.getParent().getG()+1 + ((int)(Math.sqrt(Math.pow((top.getCol()-end.getCol()),2)+Math.pow(top.getRow()-end.getRow(),2))));
                    if(open.contains(top) && top.getF() > check){top.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(top); neighbors.add(nodes[curr.getCol()][curr.getRow()+1]);}
                }catch(NullPointerException npe){}
            }catch(ArrayIndexOutOfBoundsException e){}
            try{ bottom = nodes[x][y+1]; 
                if(!open.contains(bottom)&&!closed.contains(bottom)){bottom.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(bottom); open.add(bottom); neighbors.add(nodes[curr.getCol()][curr.getRow()-1]);}
                try{
                    int check = bottom.getParent().getG()+1 + ((int)(Math.sqrt(Math.pow((bottom.getCol()-end.getCol()),2)+Math.pow(bottom.getRow()-end.getRow(),2))));
                    if(open.contains(bottom) && bottom.getF() > check){bottom.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(bottom); neighbors.add(nodes[curr.getCol()][curr.getRow()-1]);}
                }catch(NullPointerException npe){}
            }catch(ArrayIndexOutOfBoundsException e){}
            try{ left = nodes[x-1][y]; 
                if(!open.contains(left)&&!closed.contains(left)){left.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(left); open.add(left); neighbors.add(nodes[curr.getCol()-1][curr.getRow()]);}
                try{
                    int check = left.getParent().getG()+1 + ((int)(Math.sqrt(Math.pow((left.getCol()-end.getCol()),2)+Math.pow(left.getRow()-end.getRow(),2))));
                    if(open.contains(left) && left.getF() > check){left.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(left); neighbors.add(nodes[curr.getCol()-1][curr.getRow()]);}
                }catch(NullPointerException npe){}
            }catch(ArrayIndexOutOfBoundsException e){}
            try{ right = nodes[x+1][y]; 
                if(!open.contains(right)&&!closed.contains(right)){right.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(right); open.add(right); neighbors.add(nodes[curr.getCol()+1][curr.getRow()]);}
                try{
                    int check = right.getParent().getG()+1 + ((int)(Math.sqrt(Math.pow((right.getCol()-end.getCol()),2)+Math.pow(right.getRow()-end.getRow(),2))));
                    if(open.contains(right) && right.getF() > check){right.setParent(nodes[curr.getCol()][curr.getRow()]); calculate(right); neighbors.add(nodes[curr.getCol()+1][curr.getRow()]);}
                }catch(NullPointerException npe){}
            }catch(ArrayIndexOutOfBoundsException e){}
            return(neighbors);
            }catch(NullPointerException e){}
        
        return(null);
    }
    
    public void runTests(){
        try{
            if(curr.getCol() == end.getCol() && curr.getRow() == end.getRow()){
                calculatePath(curr);
                animate();
                return;
            }
        }catch(NullPointerException e){
            fail();
        }
        ArrayList<Node> neighbors = findNeighbors();
        curr = open.poll();
        closed.add(curr);
        runTests();
    }
    
    public void calculatePath(Node node){
        if(nodes[node.getCol()][node.getRow()].getParent() == null){
            return;
        }else{
            finPath.add(0, nodes[node.getCol()][node.getRow()].getParent());
            calculatePath(nodes[node.getCol()][node.getRow()].getParent());
        }
    }
    
    
    public void animate(){
        finPath.remove(0);
        for(Node node : finPath){
            buttons[node.getRow()][node.getCol()].setBackground(Color.blue);
        }
        success();
    }
    
    public void success(){
        Object[] options = {"Exit"};
        int i = JOptionPane.showOptionDialog(null,
                   "A solution was found!","Info",
                   JOptionPane.DEFAULT_OPTION,
                   JOptionPane.INFORMATION_MESSAGE,
                   null,
                   options,
                   options[0]);
        if(i==0){
            System.exit(0);
        }
    }
    
    public void fail(){
        Object[] options = {"Exit"};
        int i = JOptionPane.showOptionDialog(null,
                   "Unfortunately, it does not appear that a solution could be found.","Info",
                   JOptionPane.DEFAULT_OPTION,
                   JOptionPane.ERROR_MESSAGE,
                   null,
                   options,
                   options[0]);
        if(i==0){
            System.exit(0);
        }
    }
}
