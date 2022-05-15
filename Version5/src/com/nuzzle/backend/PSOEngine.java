package com.nuzzle.backend;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class representing the PSO Engine. This class implements all the necessary methods for initializing the swarm,
 * updating the velocity and position vectors, determining the fitness of particles and finding the best particle.
 */
public class PSOEngine {

    State initState;
    Random randomGenerator = new Random();

    int numParticles = 30; //Number of particles in swarm
    int maxIterations = 10000; //Max number of iterations
    double c1 = 0.4; //Cognitive coefficient
    double c2 = 0.6; //Social coefficient
    double w = 0.729844; //Inertia coefficient


    public PSOEngine (int numParticles, int state, int maxIterations, double c1, double c2, double w ) {
        this.initState = new State(state, 3);
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
        this.c1 = c1;
        this.c2 = c2;
        this.w = w;
    }
    List<Integer> initPostion(){
        List<Integer> ret = new ArrayList<>();
        for (int i=0; i<10; i++)
            ret.add(Arrays.asList(-1,1,3,-3).get(randomGenerator.nextInt(4)));
        return ret;
    }

    /**
     * Method to initialize the particles for PSO
     * @param particles The set of particles to initialize
     */
    public void initParticles(Particle[] particles) {
        //For each particle
        for (int i=0; i<particles.length;i++) {
            List<Integer> positions;
            int velocities;
            //For each dimension of the particle assign a random x value [-5.12,5.12] and velocity=0
            positions = initPostion();//TODO//((Math.random()* ((5.12-(-5.12)))) - 5.12);
            //System.out.println("init="+positions);
            velocities = 2;
            //Create the particle
            particles[i] = new Particle(positions, velocities);
            //Set particles personal best to initialized values
            particles[i].personalBest = particles[i].position;
        }
    }

    /**
     * Method to update the velocities vector of a particle
     * @param particle The particle to update the velocity for
     */
    public void updateVelocity(Particle particle, long best, double r1, double r2) {
        //First we clone the velocities, positions, personal and neighbourhood best
        int velocities = particle.velocity;
        long personalBest = Particle.encode(particle.personalBest);
        long positions = Particle.encode(particle.position);
        long bestNeigh = best;

        double inertiaTerm;
        double difference1;
        double difference2;

        double c1Timesr1;
        double c2Timesr2;

        double cognitiveTerm;
        double socialTerm;

    //Calculate inertia component
        inertiaTerm = w*velocities;
        //System.out.println("inertia=" + inertiaTerm);
    //Calculate the cognitive component

    //Calculate personal best - current position
        difference1 = personalBest - positions;
        //System.out.println("diff1=" + difference1);


        //Calculate c1*r1
        c1Timesr1 = c1*r1;

    //Calculate c1*r1*diff = cognitive term
        cognitiveTerm = c1Timesr1*difference1;

    //Calculate the social term

    //Calculate neighbourhood best - current position
        difference2 = bestNeigh - positions;

    //Calculate c2*r2
        c2Timesr2 = c2*r2;
    //Calculate c2*r2*diff2 = social component
        socialTerm = c2Timesr2*difference2;

    //Update particles velocity at all dimensions
        //System.out.println("velocity is : "+ (inertiaTerm+cognitiveTerm+socialTerm));
        particle.velocity = (int)(inertiaTerm+cognitiveTerm+socialTerm);
    }

    /**
     * Method to update the positions vector of a particle
     * @param particle The particle to update the position for
     */

    public void updatePosition(Particle particle) {
        //Since new position is ALWAYS calculated after calculating new velocity, it is okay to just add old position to the current velocity (as velocity would have already been updated).
        particle.position = State.validate(Particle.decode(Particle.encode(particle.position)+particle.velocity), initState);
        particle.improve();
        particle.currentState = State.applyPath(particle.position, initState);
    }

    /**
     * Method to find the best (fittest) particle from a given set of particles
     * @param particles The collection of particles to determine the best from
     * @return The best (fittest) particle from the collection of particles
     */
    public long findBest(Particle[] particles) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        long best=0L;
        int bestFitness = Integer.MAX_VALUE;
        for(int i=0; i<numParticles; i++) {
            if (evaluateFitness(particles[i].personalBest)<= bestFitness) {
                bestFitness = evaluateFitness(particles[i].personalBest);
                best = Particle.encode(particles[i].personalBest);
            }
        }
        return best;
    }

    /**
     * Method to calculate the fitness of a particle using the Rastrigin function
     * @param position The position vector to evaluate the fitness for
     * @return The fitness of the particle
     */
    public int evaluateFitness(List<Integer> position) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return computeCost(new State(123804765, 3), State.applyPath(position, initState));
    }

    public int computeCost(State goal, State current) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Heuristics heuristic = new Heuristics();
        Method h1 = Heuristics.class.getMethod("hManhattan", State.class, State.class);
        Method h2 = Heuristics.class.getMethod("hHamming", State.class, State.class);
        Method h3 = Heuristics.class.getMethod("hEuclid", State.class, State.class);
        return (int) h1.invoke(heuristic, current, goal);
    }
}