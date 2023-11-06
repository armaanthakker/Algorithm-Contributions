/**
 * Class that implements an AVL tree which implements the MyMap interface.
 * @author Brian S. Borowski
 * @version 1.1 October 24, 2023
 */
public class AVLTreeMap<K extends Comparable<K>, V> extends BSTMap<K, V>
        implements MyMap<K, V> {
    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * Creates an empty AVL tree map.
     */
    public AVLTreeMap() { }

    public AVLTreeMap(Pair<K, V>[] elements) {
        insertElements(elements);
    }

    /**
     * Creates a AVL tree map of the given key-value pairs. If
     * sorted is true, a balanced tree will be created via a divide-and-conquer
     * approach. If sorted is false, the pairs will be inserted in the order
     * they are received, and the tree will be rotated to maintain the AVL tree
     * balance property.
     * @param elements an array of key-value pairs
     */
    public AVLTreeMap(Pair<K, V>[] elements, boolean sorted) {
        if (!sorted) {
            insertElements(elements);
        } else {
            root = createBST(elements, 0, elements.length - 1);
        }
    }

    /**
     * Recursively constructs a balanced binary search tree by inserting the
     * elements via a divide-snd-conquer approach. The middle element in the
     * array becomes the root. The middle of the left half becomes the root's
     * left child. The middle element of the right half becomes the root's right
     * child. This process continues until low > high, at which point the
     * method returns a null Node.
     * @param pairs an array of <K, V> pairs sorted by key
     * @param low   the low index of the array of elements
     * @param high  the high index of the array of elements
     * @return      the root of the balanced tree of pairs
     */
    protected Node<K, V> createBST(Pair<K, V>[] pairs, int low, int high) {
        // Write code to create the balanced BST, as you did in the previous assignment.
        // The node reference must be called parent.
        if (low > high) {
            return null;
        }
        int mid = low + (high - low) / 2;
        Pair<K, V> pair = pairs[mid];
        Node<K, V> parent = new Node<>(pair.key, pair.value);
        size++;
        parent.left = createBST(pairs, low, mid - 1);
        if (parent.left != null) {
            parent.left.parent = parent;
        }
        parent.right = createBST(pairs, mid + 1, high);
        if (parent.right != null) {
            parent.right.parent = parent;
        }

        parent.height = Math.max(avlHeight(parent.left), avlHeight(parent.right)) + 1;

        return parent;
    }

    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is replaced
     * by the specified value.
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    @Override
    public V put(K key, V value) {
        NodeOldValuePair nvp = new NodeOldValuePair(null, null);
        nvp = insertAndBalance(key, value, root, nvp);
        return nvp.oldValue;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    public V remove(K key) {
        // Replace the line with the code required for proper removal from an
        // AVL tree. This task is extra credit.

        NodeOldValuePair nvp = new NodeOldValuePair(null, null);
        nvp = remove(key, root, nvp);

        if (nvp.oldValue != null) {
            size--;
        }
        return nvp.oldValue;
    }


    private NodeOldValuePair remove(K key, Node<K, V> t, NodeOldValuePair nvp) {
        if (t == null) {
            return nvp;
        }

        int comparison = key.compareTo(t.key);

        if (comparison < 0) {
            t.left = remove(key, t.left, nvp).node;
            if (t.left != null) {
                t.left.parent = t;
            }
        }
        else if (comparison > 0) {
            t.right = remove(key, t.right, nvp).node;
            if (t.right != null) {
                t.right.parent = t;
            }
        }
        // 2 children
        else if (t.left != null && t.right != null) {

            nvp.oldValue = t.value;

            Node<K, V> min = treeMinimum(t.right);
            t.key = min.key;
            t.value = min.value;
            // Searches for min value in right subtree and deletes that
            t.right = remove(min.key, t.right, nvp).node;
            if (t.right != null) {
                t.right.parent = t;
            }
        }
        // <=1 child.
        else {

            if (t.parent == null) {
                root = null;
            }

            Node<K, V> oldT = t;
            // Delete leaf node
            if (t.left != null) {
                t.left.parent = t.parent;
                t = t.left;
            } else {
                if (t.right != null) {
                    t.right.parent = t.parent;
                }
                t = t.right;
            }

            if (nvp.oldValue == null) {
                nvp.oldValue = oldT.value;
            }
        }


        if (t != null && t.parent == null) {
            root = t;
        }


        Node<K, V> n = balance(t);
        nvp.node = n;
        return nvp;
    }

    private NodeOldValuePair insertAndBalance(
            K key, V value, Node<K, V> t, NodeOldValuePair nvp) {
        if (t == null) {
            size++;
            nvp.node = new Node<K, V>(key, value);
            if (root == null) {
                root = nvp.node;
            }
            return nvp;
        }
        int comparison = key.compareTo(t.key);

        // Complete the missing section of code here.
        if (comparison < 0) {
            t.left = insertAndBalance(key, value, t.left, nvp).node;
            if (t.left != null) {
                t.left.parent = t;
            }
        }
        else if (comparison > 0) {
            t.right = insertAndBalance(key, value, t.right, nvp).node;
            if (t.right != null) {
                t.right.parent = t;
            }
        } else {

            nvp.oldValue = t.value;
            t.value = value;
        }

        Node<K, V> n = balance(t);
        nvp.node = n;
        return nvp;
    }

    private Node<K, V> balance(Node<K, V> t) {
        if (t == null) {
            return t;
        }
        // Left subtree is taller
        if(avlHeight(t.left) - avlHeight(t.right) > ALLOWED_IMBALANCE) {

            if (avlHeight(t.left.left) >= avlHeight(t.left.right)) {
                t = rotateWithLeftChild(t);
            } else {

                t = doubleWithLeftChild(t);
            }
        } else if (avlHeight(t.right) - avlHeight(t.left) > ALLOWED_IMBALANCE) {
            if (avlHeight(t.right.right) >= avlHeight(t.right.left)) {
                t = rotateWithRightChild(t);
            } else {
                t = doubleWithRightChild(t);
            }
        }

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    private int avlHeight(Node<K, V> t) {
        return t == null ? -1 : t.height;
    }

    private Node<K, V> rotateWithLeftChild(Node<K, V> k2) {

        // k2 right, k1 root.
        Node<K, V> k1 = k2.left;
        k2.left = k1.right;
        if (k1.right != null) {
            k1.right.parent = k2;
        }
        k1.right = k2;
        k1.parent = k2.parent;
        k2.parent = k1;
        k2.height = Math.max(avlHeight(k2.left), avlHeight(k2.right)) + 1;
        k1.height = Math.max(avlHeight(k1.left), k2.height) + 1;
        if (k1.parent == null) {
            root = k1;
        }
        return k1;
    }

    private Node<K, V> rotateWithRightChild(Node<K, V> k1) {
        Node<K, V> k2 = k1.right;
        k1.right = k2.left;
        if (k2.left != null) {
            k2.left.parent = k1;
        }
        k2.left = k1;
        k2.parent = k1.parent;
        k1.parent = k2;
        k1.height = Math.max(avlHeight(k1.left), avlHeight(k1.right)) + 1;
        k2.height = Math.max(avlHeight(k2.right), k1.height) + 1;
        if (k2.parent == null) {
            root = k2;
        }
        // Return k2, new root of subtree
        return k2;
    }

    private Node<K, V> doubleWithLeftChild(Node<K, V> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    private Node<K, V> doubleWithRightChild(Node<K, V> k3) {
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    private class NodeOldValuePair {
        Node<K, V> node;
        V oldValue;

        NodeOldValuePair(Node<K, V> n, V oldValue) {
            this.node = n;
            this.oldValue = oldValue;
        }
    }

    public static void main(String[] args) {
        boolean usingInts = true;
        if (args.length > 0) {
            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                usingInts = false;
            }
        }

        AVLTreeMap avlTree;
        if (usingInts) {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer>[] pairs = new Pair[args.length];
            for (int i = 0; i < args.length; i++) {
                try {
                    int val = Integer.parseInt(args[i]);
                    pairs[i] = new Pair<>(val, val);
                } catch (NumberFormatException nfe) {
                    System.err.println("Error: Invalid integer '" + args[i]
                            + "' found at index " + i + ".");
                    System.exit(1);
                }
            }
            avlTree = new AVLTreeMap<Integer, Integer>(pairs);
        } else {
            @SuppressWarnings("unchecked")
            Pair<String, String>[] pairs = new Pair[args.length];
            for (int i = 0; i < args.length; i++) {
                pairs[i] = new Pair<>(args[i], args[i]);
            }
            avlTree = new AVLTreeMap<String, String>(pairs);
        }

        System.out.println(avlTree.toAsciiDrawing());
        System.out.println();
        System.out.println("Height:                   " + avlTree.height());
        System.out.println("Total nodes:              " + avlTree.size());
        System.out.printf("Successful search cost:   %.3f\n",
                avlTree.successfulSearchCost());
        System.out.printf("Unsuccessful search cost: %.3f\n",
                avlTree.unsuccessfulSearchCost());
        avlTree.printTraversal(PREORDER);
        avlTree.printTraversal(INORDER);
        avlTree.printTraversal(POSTORDER);
    }
}
