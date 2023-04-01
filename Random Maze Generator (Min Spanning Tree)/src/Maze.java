import java.util.*;

public class Maze {

    MatrixGraph maize;
    int width;
    int height;
    int size;
    int[] rep;  //array of representatives of components

    Random danny = new Random();    //Random.nextDouble() is "both more efficient and less biased than Math.random()" according to https://stackoverflow.com/questions/738629/math-random-versus-random-nextintint

    public Maze(int height, int width){

        this.width = width;
        this.height = height;
        size = height*width;
        maize = new MatrixGraph(size, false);  //too corny?

        populateMaze(height, width);
    }

    void populateMaze(int height, int width) {

        int i = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (x < width - 1) maize.addEdge(i, i + 1, danny.nextDouble()); //add right edge

                if (!(y == height - 1)) maize.addEdge((i), ((i) + width), danny.nextDouble());  //add bottom edge

                i++;
            }
        }
    }

    void initialiseComponents(){

        rep = new int[size];

        //each vertex is initialised as its own representative
        for (int i = 0; i < size; i++) {

            rep[i] = i;

        }
    }

    void mergeComponents(int x, int y){ //representative of y set to representative of x

        int repY = rep[y];

        for (int i = 0; i < rep.length; i++) {
            if (Integer.valueOf(rep[i]).equals(repY)) {

                rep[i] = rep[x];
            }
        }
    }

    void spanningTree() {

        initialiseComponents();
        MatrixGraph corn = new MatrixGraph(size, false);

        iterate(corn);
        maize = corn;   //this is what it was all leading up to
    }

    void iterate(MatrixGraph corn){

            for (int index = 0; index < size; index++) {

                ArrayList<Integer[]> neighbors = new ArrayList<>();

                int[] all = maize.inNeighbours(index); //add All neighbors of node

                for (int neighbor : all) {

                    if (!(rep[neighbor] == rep[index]))
                        neighbors.add(new Integer[]{index, neighbor}); //add valid neighbors of node to neighbors

                }

                if(neighbors.size()>0) {

                    Integer[] minNeighbor = neighbors.get(0);

                    if (neighbors.size() > 1) {

                        for (int i = 1; i < neighbors.size(); i++) {

                            Integer[] curr = neighbors.get(i);

                            double curWeight = maize.weight(curr[0], curr[1]);
                            double minWeight = maize.weight(minNeighbor[0], minNeighbor[1]);

                            if (curWeight <= minWeight) {

                                minNeighbor = curr;

                            }
                        }
                    }

                    int kernel = minNeighbor[0];    //the jests continue
                    int neighbor = minNeighbor[1];

                    corn.addEdge(kernel, neighbor, maize.weight(kernel, neighbor));
                    corn.addEdge(neighbor, kernel, maize.weight(kernel, neighbor));
                    mergeComponents(kernel, neighbor);
                }
            }

        boolean done = true;

        for (int i = 0; i < rep.length; i++) {

            if (i!= rep.length-1 && rep[i]!=rep[i+1]){

                done = false;
                break;

            }
        }

        if (!done) iterate(corn);
    }

    void print() {

        int index = 0;
        int startY = danny.nextInt(height); //random position of start
        int endY = danny.nextInt(height);   //random position of end

        for (int y = 0; y < height; y++) {

            //StringBuilder is a mutable string variable; more efficient when many changes need to be made to string
            StringBuilder rOut = new StringBuilder(); //vertices and horizontal edges
            StringBuilder bOut = new StringBuilder(); //vertical edges

            for (int x = 0; x < width; x++) {

                if (y == startY) { rOut.append("*"); startY = -1;}  //add vertices to string
                else rOut.append((y == endY && x == width - 1) ? "*" : "+");

                rOut.append((maize.isEdge(index, index + 1)) ? ("----") : ("    "));  //add right edge to string
                bOut.append(maize.isEdge(index, index + width) ? "|    " : "     ");  //add bottom edge

                index++;
            }

            System.out.println(rOut);
            System.out.println(bOut);
        }
    }

    public static void main(String[] args) {

        Maze cob = new Maze(10, 10);
        cob.spanningTree();
        cob.print();

    }
}