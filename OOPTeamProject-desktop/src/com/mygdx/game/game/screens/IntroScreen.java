package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameMaster;

public class IntroScreen implements Screen {
    private GameMaster game;
    private Texture introBackground;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout layout; 

    public IntroScreen(GameMaster game) {
        this.game = game;
        this.batch = game.batch;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        this.font = new BitmapFont(Gdx.files.internal("simulationLC/textfont.fnt"));
        introBackground = new Texture("simulationLC/background3.png");
        
        font.getData().setScale(1.5f); // Scale the font up for better readability
        font.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        // Prepare any resources or set up initial state
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        
        batch.begin();
        batch.draw(introBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        String text = "Welcome to Voyage of the Cosmic Alphabet!\r\n"
        		+ "\r\n"
        		+ "Embark on an extraordinary journey with James,\r\n"
        		+ "a daring astronaut on a mission to explore planet for humanity's future.\r\n"
        		+ "\r\n"
        		+ " ---------Mission Brief:--------- \r\n"
        		+ "Collect the scattered alphabets for Neptune to piece together crucial data.\r\n"
        		+ "\r\n"
        		+ "Use Arrow Keys to move left & right.\r\n"
        		+ "Use Spacebar to Jump. \r\n"
        		+ "\r\n"
        		+ "Click anywhere to begin.";
        layout.setText(font, text, Color.WHITE, Gdx.graphics.getWidth(), Align.center, true);

       
        float textX = 0;
        float textY = (Gdx.graphics.getHeight() + layout.height) / 2;
       
        
        // Start drawing shapes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Use Line type for a hollow rectangle
        shapeRenderer.setColor(Color.WHITE); // Set the color of the border
        shapeRenderer.end();

        // Draw text over the border (should be done after the shapeRenderer.end())
        
        font.draw(batch, layout, textX, textY);
        batch.end();
        
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game.batch));
        }
        
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        introBackground.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
