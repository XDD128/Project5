import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Comparator;
class AStarPathingStrategy
        implements PathingStrategy
{
	class Node
{
	private Point point;
	private Node prior;
	private int g;
	private int h;
	private int f;

	public Node(Point point, Node prior, int g, int h, int f)
	{

		this.point = point;
		this.prior = prior;
		this.g = g;
		this.h = h;
		this.f = f;
	}
	public Node(Point point)
	{
		this.point = point;
	}

	public void setG(int g) {this.g = g;}
	public void setH(int h) {this.h = h;}
	public void setF(int f) {this.f = f;}

	public int getG() {return g;}
	public int getH() {return h;}
	public int getF() {return f;}

	public Point getPoint()
	{
		return point;
	}
	public Node getPrior() { return prior;}

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (o.getClass() != getClass())
			return false;
		return point.equals(((Node)o).getPoint());
	}
}

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        LinkedList<Point> path = new LinkedList<Point>();
        // int bestG = 0;
        // int 
        // // 2. Create 2 Priority Queues
        // // 

        Set<Point> closedList = new HashSet<>();
        Comparator<Node> nodeComp = Comparator.comparing(Node::getF);

        Map<Point, Integer> gValues = new HashMap<>();
        gValues.put(start, 0);
        //make a priority queue based on lowest fValue
        PriorityQueue<Node> openList = new PriorityQueue<>(nodeComp);

        Node currNode = new Node(start);
		openList.add(currNode);

        while (!withinReach.test(currNode.getPoint(), end))
        {	

        	List<Point> possibleNodes = potentialNeighbors.apply(currNode.getPoint())
        								.filter(canPassThrough)
        								.filter(p -> !p.equals(start)
        										&&	!p.equals(end))
        								.filter(p -> !closedList.contains(p))
        								.collect(Collectors.toList());

        	int g = currNode.getG() + 1;
        	for (Point p : possibleNodes)
        	{
        		int h = Math.abs(p.x - end.x) + Math.abs(p.y - end.y);
        		Node pNode = new Node(p, currNode, g, h, g+h);

        		if (!openList.contains(pNode))
				{
					openList.add(pNode);
				}

				int newG = gValues.get(currNode.getPoint()) + 1;
				if (!gValues.containsKey(p) || gValues.get(p) > newG)
					{
						gValues.put(p, newG);
						openList.remove(pNode);
						openList.add(pNode);
					}
			}
				closedList.add(currNode.getPoint());
				openList.remove(currNode);
				currNode = openList.peek();
				if (currNode == null)
					return path;
				}

        	
        if (withinReach.test(currNode.getPoint(), end))
        {
        	while (currNode.getPrior() != null)
        		{
        			path.addFirst(currNode.getPoint());
        			currNode = currNode.getPrior();
        		}
        	        	
        }


        return path; 
   }



    
}
