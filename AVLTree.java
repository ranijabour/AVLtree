
/**
*
* AVLTree
*
* An implementation of a AVL Tree with
* distinct integer keys and info
*
*/

public class AVLTree {

	IAVLNode root, min, max;
	/**
	 * public AVLTree()
	 * 
	 * Constructor for when the Tree gets no IAVLNode, then it sets the root to null
	 * O(1)
	 */
	public AVLTree() {
		root = min = max = null;
	}
	/**
	 * public AVLTree(IAVLNode x)
	 * 
	 * Constructor that recives a node, and it sets as the root of the tree
	 * saves the parent of it as null
	 * it finds the minumal node in the tree by going to the left most node in the tree O(log n)
	 * it finds the maximal node in the tree by going to the right most node in the tree O(log n)
	 * O(1) + O(log n) + O(log n) = O(log n)
	 */
	public AVLTree(IAVLNode x) {
		if(x!=null && x.isRealNode()) {
		root = new AVLNode(x.getKey(),x.getValue());
		root.setRight(x.getRight());
		root.setLeft(x.getLeft());
		root.setParent(null);
		updateMinMax();
		}
		else {
			root=null;
		}
	}
	  /**
	   * public boolean empty()
	   *
	   * returns true if and only if the tree is empty
	   *
	   *O(1): it checks exactly two things;
	   *      1. if the root = null, which costs O(1)
	   *      2. if the root is a real node, which is a function in IAVLNode classes, it  costs O(1)
	   *     => O(1) + O(1) = O(1)
	   */
	public boolean empty() {
   	return (root==null || !root.isRealNode());
   }

/**
  * public String search(int k)
  *
  * returns the info of an item with key k if it exists in the tree
  * otherwise, returns null
  * 
  * it uses the function searchNode(int k) to get the node which has the key k
  * if it exists, it returns it's value, otherwise it returns null
  * O(log n)
  */
   public String search(int k){
	  IAVLNode node = searchNode(k);
	  if(node!=null) {
		  return node.getValue();
	  }
	  return null;
 }
  /**
   * public IAVLNode searchNode(int k)
   * 
   * returns the node of an item with the key k it it exists in the tree 
   * otherwise, returns null
   * 
   * it starts from the root and does a binary search on the tree until it gets to the node needed, or returns null
   * O(log n)
   */
	public IAVLNode searchNode(int k) {
 	  if(empty()) { //check if the tree is empty
 		  return null;
 	  }
	  if(root.getKey()==k) { //check if root has the key k
		  return root;
	  }
	  IAVLNode temp = root; 
	  while(temp !=null && temp.isRealNode()) { //while we didn't reach an external leaf
		  if(temp.getKey()>k) {
			  temp=temp.getLeft();
		  }
		  else if(temp.getKey()<k) {
			  temp=temp.getRight();
		  }
		  else {
			  return temp;
		  }
	  }
	  return null;
   }

 /**
  * public int insert(int k, String i)
  *
  * inserts an item with key k and info i to the AVL tree.
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
  * promotion/rotation - counted as one rebalance operation, double-rotation is counted as 2.
  * returns -1 if an item with key k already exists in the tree.
  * 
  * inserting the node takes O(log n)
  * call of rebalanceInsert which costs O(log n)
  * => complexity of this function is : O(log n) + O(log n)+ O(log n) = O(log n)
  */
	
   public int insert(int k, String i) {
	   int cntRotations = 0;
	   if(searchNode(k)!=null) { //check if already exists
		   return -1;
	   }
	   IAVLNode x = new AVLNode(k,i);
	   if(empty()) { //in case the tree is empty
		   root = x;
		   min = max = x;
		   return 0;
	   }
	   IAVLNode temp = root;
	   boolean flag= true;
	   while(flag && temp!=null && temp.isRealNode()) { //find the right place to add the node
		   if(temp.getKey()>k) {
			   if(temp.getLeft()!=null && temp.getLeft().isRealNode()) {
				   temp=temp.getLeft();
			   }
			   else {
				   temp.setLeft(x);
				   temp.updateHeightSize();
				   x.setParent(temp);
				   flag=false;;
			   }
		   }
		   else {
			   if(temp.getRight()!=null && temp.getRight().isRealNode()) {
				   temp=temp.getRight();
			   }
			   else {
				   temp.setRight(x);
				   temp.updateHeightSize();
				   x.setParent(temp);
				   flag=false;
			   }
		   }
	   }
	   cntRotations = rebalanceInsert(x); 
	   updateMinMax();
	   x.updateHeightSize();
	   root.updateHeightSize();
	   return cntRotations;	
  }
   /**
    * public void leftRotation(IAVLNode x)
    * 
    * it receives a node, and does a left rotation to the current tree on this given node
    * all the functions it calls cost O(1)
    * ==> it's complexity is also O(1) since it has no loops
    * 
    */
   public void leftRotation(IAVLNode x) {
		IAVLNode temp = x.getRight();
		x.setRight(temp.getLeft());
		x.updateHeightSize();
		if(temp.getLeft() != null) {
			temp.getLeft().setParent(x);
		}
		temp.setParent(x.getParent());
		if (x.getParent() == null) {
			this.root = temp;
			root.updateHeightSize();
		}
		else if(x == x.getParent().getLeft()) {
			x.getParent().setLeft(temp);
			x.getParent().updateHeightSize();
		}
		else {
			x.getParent().setRight(temp);
			x.getParent().updateHeightSize();
		}
		temp.setLeft(x);
		x.setParent(temp);
		root.setParent(null);
		x.updateHeightSize();
	    temp.updateHeightSize();
	    root.updateHeightSize();
	}
   /**
    * public void rightRotation(IAVLNode x)
    * 
    * it receives a node, and does a right rotation to the current tree on this given node
    * all the functions it calls cost O(1)
    * ==> it's complexity is also O(1) since it has no loops 
    */
   public void rightRotation(IAVLNode x) {
		IAVLNode temp = x.getLeft();
		x.setLeft(temp.getRight());
		x.updateHeightSize();
		if(temp.getRight() != null) {
			temp.getRight().setParent(x);
		}
		temp.setParent(x.getParent());
		if (x.getParent() == null) {
			this.root = temp;
			root.updateHeightSize();
		}
		else if(x == x.getParent().getRight()) {
			x.getParent().setRight(temp);
			x.getParent().updateHeightSize();
		}
		else {
			x.getParent().setLeft(temp);
			x.getParent().updateHeightSize();
		}
		temp.setRight(x);
		x.setParent(temp);
		root.setParent(null);
		x.updateHeightSize();
	    temp.updateHeightSize();
	    root.updateHeightSize();
	}
  /**
   *public int rebalanceInsert(IAVLNode x)
   *
   * this function is called after insert (or joining two trees) to start rebalancing the current tree
   * starting from the given node x
   * it uses right and left rotations when needed, and goes from x up to the root,
   * which means maximum log n entries to the loop
   * counts and returns the counter of the rotations
   * 
   * ==> log n*O(1) = O(log n)
   */
   public int rebalanceInsert(IAVLNode x) {
      IAVLNode temp = x;
      int cntRotations = 0 ;
	   while(temp!=null && temp.getParent()!=null) { //while did not reach the root
		   temp.updateHeightSize();
		   temp.getParent().updateHeightSize();
		   int BF= temp.BFCalc();
		   int BFParent = temp.getParent().BFCalc();
		   if(BFParent==2) {
			   if(BF==1) {
				   rightRotation(temp.getParent());
				   cntRotations++;
				   
			   }
			   if(BF==-1) {
				   IAVLNode t= temp.getParent();
				   leftRotation(temp);
				   rightRotation(t);
				   cntRotations+=2;
				   
			   }
		   }
		   else if(BFParent==-2) {
			   if(BF==-1) {
				   leftRotation(temp.getParent());
				   cntRotations++;
				   
			   }
			   else if(BF==1) {
				   IAVLNode t= temp.getParent();
				   rightRotation(temp);
				   leftRotation(t);
				   cntRotations+=2;
				   
			   }
		   }
		   temp.updateHeightSize();
		   temp = temp.getParent();
	   }
	   return cntRotations;
   }
   /**
  * public int delete(int k)
  *
  * deletes an item with key k from the binary tree, if it is there;
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
  * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
  * returns -1 if an item with key k was not found in the tree.
  * 
  * it uses findSuccessor to replace it with the deleted node if needed which costs O(log n)
  * it uses rebalanceDelted which costs O(log n) after deleting the node
  * 
  * ==> O(log n)+O(log n)+O(log n) = O(log n)
  */
   public int delete(int k)
  {
	   IAVLNode node = searchNode(k);
	   if(node==null) {
		   return -1;
	   }
	   IAVLNode parent = node.getParent();
	   if(!node.getLeft().isRealNode() && !node.getRight().isRealNode()) {
		   if (parent== null) {
			   root = null;
			   return 0;
		   }

		   else if( parent.getLeft()==node) {
			   parent.setLeft(new VirtualNode());
			   return rebalanceDelete(parent);
		   }
		   else {
			   parent.setRight(new VirtualNode());
			   return rebalanceDelete(parent);
		   }
	   }
	   else if(!node.getLeft().isRealNode() || !node.getRight().isRealNode()) {
		   if(parent==null) {
			   if( node.getLeft().isRealNode()) {
				   root = node.getLeft();
				   root.setParent(null);
				   return 0;
				  
			   }
			   else {
				   root = node.getRight();
				   root.setParent(null);
				   return 0;
				   
			   }
		   }
		   else if(node.getLeft().isRealNode()) {
			   if(parent.getLeft()==node) {
				   parent.setLeft(node.getLeft());
				   node.getLeft().setParent(parent);
				   return rebalanceDelete(parent);
			   }
			   else {
				   parent.setRight(node.getLeft());
				   node.getLeft().setParent(parent);
				   return rebalanceDelete(parent);
			   }
		   }
		   else{
			   if(parent.getLeft()==node) {
				   parent.setLeft(node.getRight());
				   node.getRight().setParent(parent);
				   return rebalanceDelete(parent);
			   }
			   else {
				   parent.setRight(node.getRight());
				   node.getRight().setParent(parent);
				   return rebalanceDelete(parent);
			   }
		   }
	   }
	   else {
		   IAVLNode succ = findSuccessor(node);
		   IAVLNode rightSucc = succ.getRight();
		   IAVLNode parentSucc = succ.getParent();
		   if(node == parentSucc && parent == null) {
			   root = succ;
			   root.setLeft(node.getLeft());
			   node.getLeft().setParent(root);
			   root.setParent(null);
			   if(rightSucc==null || !rightSucc.isRealNode()) {
				   return rebalanceDelete(root.getLeft());
			   }
			   else {
				   return rebalanceDelete(rightSucc);
			   }
		   }
		   else if(parentSucc.getLeft()==succ) {
			   succ.setLeft(node.getLeft());
			   succ.setRight(node.getRight());
			   succ.setParent(parent);
			   if( parent !=null && parent.getLeft()==node) {
				   parent.setLeft(succ);
			   }
			   else if(parent!=null){
				   parent.setRight(succ);
			   }
			   parentSucc.setLeft(rightSucc);
			   rightSucc.setParent(parentSucc);
			   return rebalanceDelete(rightSucc);
		   }
		   else {
			   succ.setLeft(node.getLeft());
			   succ.setRight(node.getRight());
			   succ.setParent(parent);
			   if( parent !=null && parent.getLeft()==node) {
				   parent.setLeft(succ);
			   }
			   else if(parent!=null){
				   parent.setRight(succ);
			   }
			   parentSucc.setRight(rightSucc);
			   rightSucc.setParent(parentSucc);
			   return rebalanceDelete(rightSucc);
		   }
	   }
  }
   /**
    *public int rebalanceDelete(IAVLNode x)
    *
    * this function is called after deleting a node to start rebalancing the current tree
    * starting from the given node x which was the parent of the deleted node
    * it uses right and left rotations when needed, and goes from x up to the root,
    * which means maximum log n entries to the loop
    * counts and returns the counter of the rotations
    * 
    * ==> log n*O(1) = O(log n)
    */
   public int rebalanceDelete(IAVLNode x) {
	   int cntRotations=0;
	   while( x!=null && x.isRealNode()) { //while x is not out of the tree
		   x.updateHeightSize();
		   int BFParent = x.BFCalc();
		   if(BFParent==2) {
			   int BF= x.getLeft().BFCalc();
			   if(BF==1 || BF==0) {
				   rightRotation(x);
				   cntRotations++;
				   
			   }
			   else if(BF==-1) {
				   leftRotation(x.getLeft());
				   rightRotation(x);
				   cntRotations+=2;
				   
			   }
		   }
		   else if(BFParent==-2) {
			   int BF=x.getRight().BFCalc();
			   if(BF==-1 || BF==0) {
				   leftRotation(x);
				   cntRotations++;
				   
			   }
			   else if(BF==1) {
				   rightRotation(x.getRight());
				   leftRotation(x);
				   cntRotations+=2;
				   
			   }
		   }
		   x=x.getParent();
	   }
	   this.updateMinMax();
	   root.updateHeightSize();
	   return cntRotations;
   }
   /**
    *public IAVLNode findSuccessor(IAVLNode x)
    *it either goes to the left most of the right child 
    *or goes up until the first right
    *
    *==> O(log n)
    *
    */
   public IAVLNode findSuccessor(IAVLNode x) {
	   IAVLNode temp = x.getRight();
	   if(temp.isRealNode()) { 
		   while(temp.getLeft().isRealNode()) { //while temp has a left
			   temp = temp.getLeft();
		   }
		   return temp;
	   }
	   else {
		   temp=x.getParent();
		   while(temp!=null && temp.isRealNode()) { //while temp is not out of the tree
			   if(temp.getKey()>x.getKey()) {
				   return temp;
			   }
			   temp=temp.getParent();
		   }
	   }
	   return null;
  }
  /**
   * public String min()
   *
   * Returns the info of the item with the smallest key in the tree,
   * or null if the tree is empty
   * 
   * O(1)
   */
   public String min()
  {
	   if ( min==null || !min.isRealNode()) {
		   return null;
	   }
	   return min.getValue(); // to be replaced by student code
  }

  /**
   * public String max()
   *
   * Returns the info of the item with the largest key in the tree,
   * or null if the tree is empty
   * 
   * O(1)
   */
   public String max() {
	  if(max==null || !max.isRealNode() ) {
		  return null;
	  }
	   return max.getValue();
  }

 /**
  * public IAVLNode[] nodeToArray()
  *
  * Returns a sorted array which contains all the nodes in the tree,
  * or an empty array if the tree is empty.
  * by calling for the successor n-1 starting from min times, which proven to be of complexity O(n)
  */
   public IAVLNode[] nodeToArray() {
   	IAVLNode[] arr = new IAVLNode[root.getSize()];
   	IAVLNode temp = min;
   	int i=0;
   	while(temp!=null && temp.isRealNode()) {
   		arr[i] = temp;
   		temp = findSuccessor(temp);
   		i++;
   	}
   	return arr;
   }
   /**
    * public int[] keysToArray()
    *
    * Returns a sorted array which contains all keys in the tree,
    * or an empty array if the tree is empty.
    * uses nodeToArray()
    * 
    * ==> O(n) + O(n) = O(n)
    */   
   public int[] keysToArray(){
       int[] arr = new int[root.getSize()]; 
       IAVLNode[] nodeArr = nodeToArray();
       for(int i=0;i<nodeArr.length;i++) {
       	arr[i] = nodeArr[i].getKey();
       }
       return arr;              
 }

 /**
  * public String[] infoToArray()
  *
  * Returns an array which contains all info in the tree,
  * sorted by their respective keys,
  * or an empty array if the tree is empty.
  * uses nodeToArray()
  * 
  * ==> O(n)+O(n)  =O(n)
  */
   public String[] infoToArray(){
       String[] arr = new String[root.getSize()];
       IAVLNode[] nodeArr = nodeToArray();
       for(int i=0;i<nodeArr.length;i++) {
       	arr[i] = nodeArr[i].getValue();
       }
       return arr;                    
 }

  /**
   * public int size()
   *
   * Returns the number of nodes in the tree.
   *
   * precondition: none
   * postcondition: none
   * 
   * O(1)
   */
 	public int size(){
	   if(root==null) {
		   return 0;
	   }
 		return root.getSize(); 
   }
  
    /**
   * public int getRoot()
   *
   * Returns the root AVL node, or null if the tree is empty
   *
   * precondition: none
   * postcondition: none
   * 
   * O(1)
   */
 	 public IAVLNode getRoot(){
	   if(root!=null) {
		   return root;
	   }
	   return new VirtualNode();
 	 }
    /**
   * public string split(int x)
   *
   * splits the tree into 2 trees according to the key x. 
   * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
   * postcondition: none
   * 
   * the height of the node that needs splitting is at most log n
   * the two trees it needs to join each step use the complexity O(1/2log(n)*2)=O(logn)
   * ==> O(log n)*O(log n)=O((logn)^2)
   */   

 	 public AVLTree[] split(int x){
 		 IAVLNode node = searchNode(x);
 		 AVLTree T1 = new AVLTree(node.getLeft());
 		 AVLTree T2 = new AVLTree(node.getRight());
 		 int height = root.getSize();
 		 AVLTree[] arrRight = new AVLTree[height];
 		 AVLTree[] arrLeft = new AVLTree[height];
 		 IAVLNode[] onRight = new AVLNode[height];
 		 IAVLNode[] onLeft=new AVLNode[height];
 		 int i=0, j=0;
 		 while(node.getParent() != null && node.getParent().isRealNode()) {
 			 if(node.getParent().getKey() > x) {
 				 AVLTree right= new AVLTree(node.getParent().getRight());
 				 arrRight[i]=right;
 				 onRight[i]=node.getParent();
 				 i++;
 			 }
 			 else {
 				 AVLTree left = new AVLTree(node.getParent().getLeft());
 				 left.getRoot().setParent(null);
 				 arrLeft[j]=left;
 				 onLeft[j]=node.getParent();
 				 j++;
 			 }
 			 node = node.getParent();
 		 }
 		 for(int k=0;k<j;k++) {
 			 IAVLNode left=new AVLNode(onLeft[k].getKey(),onLeft[k].getValue());
 			 T1.join(left, arrLeft[k]);
 		 }
 		 for(int k=0;k<i;k++) {
 			 IAVLNode right=new AVLNode(onRight[k].getKey(),onRight[k].getValue());
 			 T2.join(right, arrRight[k]);
 		 }
 		 T1.updateMinMax();
 		 T2.updateMinMax();
 		 AVLTree[] arr = {T1,T2};
 		 return arr;
 	 }
  /**
   * public join(IAVLNode x, AVLTree t)
   *
   * joins t and x with the tree. 	
   * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
   * postcondition: none
   * 
   * as stated, the complexity of join is (|tree.rank - t.rank| + 1), because this is the longest number of entries
   * until finding the right place to connect both trees
   * after connecting them, starting from x, the function uses rebalanceInsert() to rebalance the tree
   * ==> O(log n - log m)+O(log (n+m)) ===? O(logn(n+m))
   */   
 	 public int join(IAVLNode x, AVLTree t){
 		 if(t.empty()) {
 			 this.insert(x.getKey(), x.getValue());
 			 return(getRoot().getHeight()+1);
 		 }
 		 else if(empty()) {
 			 t.insert(x.getKey(), x.getValue());
 			 this.root = t.getRoot();
 			 return(t.getRoot().getHeight()+1);
 		 }
 		 IAVLNode longTree, shortTree;
 		 if(root.getHeight()-t.getRoot().getHeight()>1) {
 			 longTree=root;
 			 shortTree=t.getRoot();
 		 }
 		 else if(t.getRoot().getHeight()-root.getHeight()>1){
 			 longTree=t.getRoot();
 			 shortTree=root;
 		 }
 		 else {
 			 root.setParent(x);
 			 t.getRoot().setParent(x);
 			 if(root.getKey()>x.getKey()) {
 				x.setRight(root);
 				x.setLeft(t.getRoot());
 			}
 			 else {
 				 x.setRight(t.getRoot());
 				 x.setLeft(root);
 			 }
 			 int retVal = Math.abs(root.getHeight()- t.getRoot().getHeight())+1;
 			 this.root = x;
 			 root.setParent(null);
 			 root.updateHeightSize();
 			 updateMinMax();
 			return retVal;
 		 }
 		 int ret = Math.abs(root.getHeight()- t.getRoot().getHeight())+1;
 		 if(shortTree.getKey()>x.getKey()) {
 			 x.setRight(shortTree);
 			 shortTree.setParent(x);
 		 }
 		 else {
 			 x.setLeft(shortTree);
 			 shortTree.setParent(x);
 		 }
 		 int height=shortTree.getHeight();
 		 root  = longTree;
 		 IAVLNode temp=longTree;
 		 boolean bigger = (temp.getKey() > x.getKey());
 		 while(temp.getHeight() - height > 1) {
 			 if(bigger) {
 				 temp=temp.getLeft();
 			 }
 			 else {
 				 temp=temp.getRight();
 			 }
 		 }
 		if(bigger) {
 			x.setRight(temp);
 			x.setParent(temp.getParent());
 			temp.getParent().setLeft(x);
 			temp.setParent(x);
 		}
 		else {
 			x.setLeft(temp);
 			x.setParent(temp.getParent());
 			temp.getParent().setRight(x);
 			temp.setParent(x);
 		}
 		rebalanceInsert(x);
 		updateMinMax();
 		return ret; 
 	 }
 	/**
	   * public void updateMinMax
	   * 
	   * a function to update the minimum and maximum nodes after inserting or delteing ones
	   * O(1)
	   */
 	 public void updateMinMax() {
 		IAVLNode temp = root;
   	    if(temp==null || !temp.isRealNode()) {
   	    	return;
   	    }
 		while(temp.getLeft().isRealNode()) {
 		   temp=temp.getLeft();	
 		   }
 	    min = temp;	   
 	    temp=root;
 	    while(temp.getRight().isRealNode()) {
 		   temp=temp.getRight();
 		   }
 	    max = temp;   
 	 }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public void updateHeightSize();
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
   		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
   		public int getSize(); //returns the size of the node, 0 if virtual
   		public void setSize(int size); // sets the size of the node
   		public int BFCalc(); // calculates the difference of heights between left and right child
	}

  /**
  * public class AVLNode
  *
  * If you wish to implement classes other than AVLTree
  * (for example AVLNode), do it in this file, not in 
  * another file.
  * This class can and must be modified.
  * (It must implement IAVLNode)
  */
 public class AVLNode implements IAVLNode{
	  	int key;
	  	String info;
	  	int height;
	  	int size;
	  	IAVLNode left, right, parent;
	  	public AVLNode() {

	  	}
	  	public AVLNode(int key, String info) {
	  		this.key=key;
	  		this.info=info;
	  		this.left = new VirtualNode();
	  		this.right = new VirtualNode();
	  		parent=null;
			this.height = 0;
			this.size = 1;
	  	}
		public int getKey()
		{
			return key; 
		}
		public String getValue()
		{
			return info; 
		}
		public void setLeft(IAVLNode node)
		{
			if(node!=null && node.isRealNode()) {
				this.left=node;
			}
			else {
				this.left = new VirtualNode();
			}
			updateHeightSize();
		}
		public IAVLNode getLeft()
		{
			return left;
		}
		public void setRight(IAVLNode node)
		{
			if(node!=null && node.isRealNode()) {
				this.right=node;
			}
			else {
				this.right = new VirtualNode();
			}
			updateHeightSize();
		}
		public IAVLNode getRight()
		{
			return right;
		}
		public void setParent(IAVLNode node)
		{
			if(node!=null && node.isRealNode()) {
				this.parent=node;
			}
			else {
				this.parent = null;
			}
		}
		public IAVLNode getParent()
		{
			return parent;
		}
   		public boolean isRealNode()
		{
			return true; 
		}
		public void setHeight(int height)
   {
     this.height=height;
   }
		public int getHeight()
   {
     return height; 
   }
		
		public int getSize() {
			return size;
		}
		
		public void setSize(int size) {
			this.size=size;
		}
		
		public int BFCalc() {
			int bf=left.getHeight()-right.getHeight();
			return bf;
		}
		//updates the height and size at the same time
		public void updateHeightSize() {
			this.height = Math.max(left.getHeight(), right.getHeight())+1;
			this.size = left.getSize() + right.getSize() +1;
		}
 	}
 
 //a class for virtual nodes, it override the functions which it has to return special values to 
 public class VirtualNode extends AVLNode{
	 public VirtualNode() {
		super();
	 }
	 @Override
	 public int getSize() {
		  return 0;
	  }
	 @Override
	  public int getHeight() {
		  return -1;
	  }
	 @Override
	  public int getKey() {
		  return -1;
	  }
	 @Override
	  public boolean isRealNode() {
		  return false;
	  }
 }

}
 


