/* Nome: Vinicius Pessoa Duarte                */
/* Numero USP: 8941043                         */
/* Disciplina: MAC-0323                        */
/* Exercicio: Number of paths in a DAG/4.2.33  */

import edu.princeton.cs.algs4.*;

public class WordDAG {
    private Digraph DG;
    private static String[] keys;
    private static int[] top;

    // Contrutor que recebe uma lista de strings e
    // cria um digrafo com as propriedades do enunciado
    public WordDAG (String[] strings) {
        DG = new Digraph (strings.length);

        for (int i = 0; i < strings.length; ++i) {
            keys = strings;
            String s = strings[i];
            for (int j = 0; j < strings.length; ++j) {
                String t = strings[j];
                if (isNeighbor (s, t) && s.compareTo (t) > 0)
                    DG.addEdge (i, j);
                if (s.length () > t.length () && t.compareTo (s.substring (0, t.length ())) == 0)
                    DG.addEdge (i, j);
            }
        }
    }

    // Imprime as arestas do digrafo
    public void PrintDag () {
        Out out = new Out ("saida.txt");
        for (int i = 0; i < DG.V () ; ++i) {
            for (int j : DG.adj (i))
                out.println (keys[i] + " " + keys[j]);
        }
    }

    // Informa quantos caminho existem de a para b
    public void PrintPathCount (String a, String b) {
        Topological TDG = new Topological (DG);

        top = new int[DG.V ()];
        int i = 0;
        for (int w : TDG.order ())
            top[i++] = w;

        int indexA = find (a);
        int indexB = find (b);

        Out out = new Out ("saida.txt");
        out.println (Count (indexA, indexB));
    }

    // Verifica se a e vizinho de b
    private static boolean isNeighbor(String a, String b) {
        if (a.length () != b.length ())
            return false;
        int differ = 0;
        for (int i = 0; i < a.length()  ; i++) {
            if (a.charAt(i) != b.charAt(i)) differ++;
            if (differ > 1) return false;
        }
        return true;
    }

    // Encontrar a posicao que representa a string s no
    // vetor que guarda a ordem topologica do digrafo
    private static int find (String s) {
        int i = 0;

        while (i < top.length && keys[top[i]].compareTo (s) != 0)
            ++i;

        if (i < top.length)
            return i;

        return -1;
    }

    // Verifica se existe aresta da string representada por indexA
    // para a string representada por indexB.
    private boolean connects (int indexA, int indexB) {
        for (int v : DG.adj (top[indexA]))
            if (v == top[indexB])
                return true;

        return false;
    }

    // Conta o numero de caminhos possiveis da string representada
    // por indexA ate a string representada por indexB
    private int Count (int indexA, int indexB) {
        int count = 0;

        for (int indexC = indexA; indexC <= indexB; ++indexC) {
            if (connects (indexA, indexC)) {
                if (indexC == indexB) count++;
                count += Count (indexC, indexB);
            }
        }

        return count;       
    }

    // Modulo principal
    public static void main (String[] args) {
        String[] strings = {"cobra", "sobra", "sobrar", "cobro", "cobrou", "sobrando", "cobre"};

        WordDAG WG = new WordDAG (strings);
        WG.PrintDag ();
        
        WG.PrintPathCount ("sobrando", "cobrou");
    }
}
