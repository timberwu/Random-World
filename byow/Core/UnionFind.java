package byow.Core;



public class UnionFind {

    int[] unionSet;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        unionSet = new int[N];
        for (int i = 0; i < N; i++) {
            unionSet[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        while (v >= 0) {
            v = parent(v);
        }
        return -v;
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return unionSet[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v >= unionSet.length) {
            throw new IllegalArgumentException();
        }
        if (unionSet[v] < 0) {
            return v;
        }
        int root = find(parent(v));
        //key point
        unionSet[v] = root;
        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 == v2 || connected(v1, v2)) {
            return;
        }
        if (sizeOf(v1) <= sizeOf(v2)) {
            int sizeOfv1 = sizeOf(v1);
            int sizeOfv2 = sizeOf(v2);
            int root1 = find(v1);
            int root2 = find(v2);
            unionSet[root1] = root2;
            unionSet[root2] = -sizeOfv1 - sizeOfv2;
            return;
        } else {
            union(v2, v1);
        }
    }

}
