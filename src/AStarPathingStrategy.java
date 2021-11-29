import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<Point>();

        Comparator<Node> compare_for_openList = Comparator.comparing(Node::getF);  //want neighbor with best F value (lowest F-value)
        PriorityQueue<Node> nextUp = new PriorityQueue<Node>(compare_for_openList); // need ordering of best F values so PQ
        HashMap<Point, Node> openList = new HashMap<Point, Node>();  //need quick look ups so Hashmaps as O(1) lookup times
        // 2 DS for openList since we need ordering by best F value AND quick lookup times

        HashMap<Point, Node> closedList = new HashMap<Point, Node>(); //need quick look ups so Hashmaps as O(1) lookup times
        // only 1 DS for closed List since we only really need quick lookup times

        Node current = new Node(start, null, 0, start.distanceSquared(end)); // create current Node
        openList.put(current.getCurrent(), current); // step 2 add current node to open list
        nextUp.add(current);  // step 2 add current node to open list

        if (openList.size() > 0){ // only loop through potential neighbors if the openList is non empty

            while ( (!withinReach.test(current.getCurrent(), end)) ) { // while openList not empty and current position not withinreach of target
                List<Point> neighbors = potentialNeighbors.apply(current.getCurrent()).filter(p -> !closedList.containsKey(p)).filter(canPassThrough).collect(Collectors.toList());  //filter potential neighbors and modify to a list
                for (Point next : neighbors) { // step 3; determine all valid adjacent nodes, cycle through each neighbor
                    Node subsequent = new Node(next, current, current.getG() + 1, next.distanceSquared(end));  //creation of adjacent node that is valid using euclidean dist for heuristic h

                    if (!openList.containsKey(subsequent.getCurrent())) { // check if adjacent node not already in it (Step 3a)
                        openList.put(subsequent.getCurrent(), subsequent);  // add neighbors to open list if not in it
                        nextUp.add(subsequent);   // add neighbors to open list if not in it
                    }

                    Node prev = openList.get(subsequent.getCurrent());  //get previously calculated node (Step 3c) using key of "subsequent" node

                    if (subsequent.getG() < prev.getG() || prev.getG() == 0) { //Step 3c calculate g value and if g val is better than prev calculated g val then update all nodes data in queue and resort, else just leave as is (or if no previously calculated g val)
                        nextUp.remove(prev);
                        nextUp.add(subsequent);
                        openList.replace(subsequent.getCurrent(), prev, subsequent);
                    }
                }


                closedList.put(current.getCurrent(), current);  //move current node to the closedList
                openList.remove(current.getCurrent());  //remove current node from openList once on closedList
                nextUp.remove(current);  //remove current node from openList once on closedList

                if (nextUp.peek() == null) {  //if openList empty that means we have searched everything, so now just return path
                    return path;
                }

                if (nextUp.peek() != null) {  // if openList not empty
                    current = nextUp.peek();   //current is now the best F value (smallest F val) node and make it the current node  // Step 5
                    nextUp.remove(current);  // once this best F val node on closed list now remove it from PQ  // Step 5
                }
            }

        }


        if (current.getPrior() != null) {

            while (current.getPrior() != null) { //keep iterating backwards until the prior node is null, ie we are back at our initial position since once we are at initial node, prior node will be null
                path.add(0, current.getCurrent());  //add this current nodes position to the top of the linkedlist
                current = current.getPrior();  // update current node to be the prior node, ie iterate backwards in our path to make our path
            }
        }

        return path;  // once you are back at the original node, ie the prior node is null since youre back at the original, then return the path list (LL)
    }
}
