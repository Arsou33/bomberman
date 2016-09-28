package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URISyntaxException;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.peekmoon.bomberman.board.Board;
import org.peekmoon.bomberman.board.Player;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.peekmoon.bomberman.shader.FragmentShader;
import org.peekmoon.bomberman.shader.ProgramShader;
import org.peekmoon.bomberman.shader.VertexShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException, URISyntaxException  {
		log.info("Bomberman starting...");
		new Main().start();
		log.info("Bomberman finished...");
	}
	
	private long window;
	private GLFWErrorCallback errorCallback;
	
	private DatagramSocket socket;
	
    private Board board;
    private Player player;
    private Camera camera;
    
    private Texture testTexture;

    private void start() throws IOException, URISyntaxException {
        initOpenGlWindow();
        
        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        shader.use();
        
        testTexture = new Texture("test.png");
        
        board = new Board(shader);
        player = new Player(board, shader);
        camera = new Camera(shader);
        
        socket = new DatagramSocket(); // Bound to an ephemeral port
        CommandSender commandSender = new CommandSender(socket);
        commandSender.register();
        StatusReceiver statusReceiver = new StatusReceiver(socket);
        statusReceiver.next();
        Thread statusReceiverThread = new Thread(statusReceiver, "Status receiver");
        statusReceiverThread.setDaemon(true); // TODO : clean stop
        statusReceiverThread.start();
        KeyManager keyManager = new KeyManager(window, commandSender, camera);
        glfwSetKeyCallback(window, keyManager);
        
        double lastTime = glfwGetTime();
        double timeToStat = 1;
        int nbFrame = 0;
        while (!glfwWindowShouldClose(window)) {
            double time = glfwGetTime();
            float elapsed = (float) (time - lastTime);
            lastTime = time;
            timeToStat -= elapsed;
            nbFrame++;
            if (timeToStat < 0) {
                log.debug("FPS : {}", nbFrame/15);
                nbFrame = 0;
                timeToStat=15;
            }
            
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            camera.update();
            keyManager.update(elapsed);
            board.update(elapsed);
            player.updateStatus(statusReceiver.getStatus());
            
            board.render();
            player.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        release();        

    }

    private void initOpenGlWindow() {
        errorCallback = GLFWErrorCallback.create((error, description) -> log.error(
                "LWJGL Error - Code: {}, Description: {}",
                Integer.toHexString(error),
                GLFWErrorCallback.getDescription(description)));
        glfwSetErrorCallback(errorCallback);
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        
        window = glfwCreateWindow(896, 504, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetWindowAspectRatio(window, 16, 9);
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> glViewport(0, 0, width, height));
        glfwMakeContextCurrent(window);
        //glfwSwapInterval(1);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
    }

    private void release() {
        socket.close();
        board.release();
        player.release();
        testTexture.release();
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        glfwTerminate();
        errorCallback.free();
    }
    
}
