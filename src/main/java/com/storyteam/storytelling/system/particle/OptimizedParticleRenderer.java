package com.storyteam.storytelling.system.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

public class OptimizedParticleRenderer implements IParticleRenderer<OptimizedParticle, RenderLevelStageEvent> {
   public static final int maxParticleCount = 100000;
   public static final int particleSize = 48;
   public static final OptimizedParticleRenderer instance = new OptimizedParticleRenderer();
   public static boolean refreshParticleRenderer = false;
   private final Map<AbstractParticleType, List<OptimizedParticle>> particlesByTypes = new HashMap<>();
   private final float[] pose = new float[16];
   int shaderProgram = -1;
   int vao = -1;
   int ssbo = -1;

   public OptimizedParticleRenderer() {
      MinecraftForge.EVENT_BUS.addListener(this::onRenderLevelStage);
   }

   @OnlyIn(Dist.CLIENT)
   public void onRenderLevelStage(RenderLevelStageEvent event) {
      if (event.getStage() == Stage.AFTER_BLOCK_ENTITIES) {
         event.getPoseStack().last().pose().get(this.pose);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void renderParticles(RenderLevelStageEvent event, Set<OptimizedParticle> particles) {
      if (event.getStage() == Stage.AFTER_LEVEL) {
         this.particlesByTypes.clear();

         for (OptimizedParticle particle : particles) {
            this.particlesByTypes.computeIfAbsent(particle.type, t -> new ArrayList<>()).add(particle);
         }

         this.initOpengl();
         GL46.glUseProgram(this.shaderProgram);
         GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(this.shaderProgram, "poseStack"), false, this.pose);
         GL46.glUniformMatrix4fv(
            GL46.glGetUniformLocation(this.shaderProgram, "projectionMatrix"), false, RenderSystem.getProjectionMatrix().get(new float[16])
         );
         GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(this.shaderProgram, "modelViewMatrix"), false, RenderSystem.getModelViewMatrix().get(new float[16]));
         Vec3 p = Minecraft.getInstance().gameRenderer.getMainCamera().position;
         GL46.glUniform3f(GL46.glGetUniformLocation(this.shaderProgram, "cameraPosition"), (float)p.x, (float)p.y, (float)p.z);
         GL46.glBindVertexArray(this.vao);

         for (AbstractParticleType type : this.particlesByTypes.keySet()) {
            if (this.particlesByTypes.get(type).size() > 100000) {
               System.err.println("Чот ты многовато партиклов спавнишь, пожалей видюху, остановись (" + this.particlesByTypes.get(type).size() + ")");
            } else {
               ByteBuffer buffer = BufferUtils.createByteBuffer(this.particlesByTypes.get(type).size() * 48);

               for (OptimizedParticle particle : this.particlesByTypes.get(type)) {
                  particle.renderToBuffer(buffer);
               }

               GL46.glBindBuffer(37074, this.ssbo);
               GL46.glBindBufferBase(37074, 0, this.ssbo);
               GL46.glBufferSubData(37074, 0L, buffer.rewind());
               GL46.glEnable(3042);
               GL46.glEnable(2929);
               GL46.glDepthMask(false);
               type.setup();
               GL46.glDrawElements(4, this.particlesByTypes.get(type).size() * 6, 5125, 0L);
            }
         }

         GL46.glBindVertexArray(0);
      }
   }

   private void initOpengl() {
      if (refreshParticleRenderer) {
         refreshParticleRenderer = false;
         this.shaderProgram = -1;
         this.vao = -1;
         this.ssbo = -1;
      }

      if (this.shaderProgram == -1) {
         String vertexShaderSource = "#version 460\nstruct Particle{\n    vec4 position;\n    vec4 color;\n    vec4 fogSettings;\n};\nlayout(std430, binding = 0) buffer Particles {\n    Particle particles[];\n};\nin int id;\nout vec2 uv;\nout vec4 color;\nuniform vec2 uv0;\nuniform vec2 uv1;\nuniform vec2 uv2;\nuniform vec2 uv3;\nuniform vec3 cameraPosition;\nuniform mat4 projectionMatrix;\nuniform mat4 modelViewMatrix;\nuniform mat4 poseStack;\n\nfloat Necr0manthFog(float d, float NECR0MANTH_FOG_COEFF1, float NECR0MANTH_FOG_COEFF2, float NECR0MANTH_FOG_EXP) {\n    float fog = d * NECR0MANTH_FOG_COEFF1;\n    fog = pow(fog, NECR0MANTH_FOG_EXP);\n    fog = exp(-NECR0MANTH_FOG_COEFF2 * fog);\n\treturn fog;\n}\nvoid main() {\n   uint particleId = id/4;\n   uint localId = id%4;\n   Particle particle = particles[particleId];\n   vec3 pos = particle.position.xyz - cameraPosition;\n   float r = particle.position.w;\n   vec3 d1 = normalize(cross(pos, vec3(0,1,0)))*r;\n   vec3 d2 = normalize(cross(pos, d1))*r;\n   if(localId/2==0){\n        pos+=d1;\n        if(localId==0){\n            uv=uv0;\n        }else{\n            uv=uv1;\n        }\n   }else{\n        pos-=d1;\n        if(localId==2){\n            uv=uv2;\n        }else{\n            uv=uv3;\n        }\n   }\n   if(localId%2==0){\n        pos+=d2;\n   }else{\n        pos-=d2;\n   }\n   color = particle.color;\n   color.a *= Necr0manthFog(length(pos), particle.fogSettings.x, particle.fogSettings.y, particle.fogSettings.z);\n   gl_Position = projectionMatrix * modelViewMatrix * poseStack * vec4(pos, 1);\n}\n";
         String fragmentShaderSource = "                    #version 460\n                    layout(location = 0) out vec4 FragColor0;\n                    in vec2 uv;\n                    in vec4 color;\n                    uniform sampler2D tex;\n                    void main() {\n                        FragColor0 = texture(tex, uv) * color;\n//                        FragColor0 = vec4(1);\n                    }\n";
         int vertexShader = GL46.glCreateShader(35633);
         int fragmentShader = GL46.glCreateShader(35632);
         GL46.glShaderSource(vertexShader, vertexShaderSource);
         GL46.glShaderSource(fragmentShader, fragmentShaderSource);
         GL46.glCompileShader(vertexShader);
         GL46.glCompileShader(fragmentShader);
         this.shaderProgram = GL46.glCreateProgram();
         GL46.glAttachShader(this.shaderProgram, vertexShader);
         GL46.glAttachShader(this.shaderProgram, fragmentShader);
         GL46.glLinkProgram(this.shaderProgram);
         if (GL46.glGetProgrami(this.shaderProgram, 35714) != 1) {
            System.out.println(GL46.glGetProgramInfoLog(this.shaderProgram));
            this.shaderProgram = -1;
         }

         if (GL46.glGetShaderi(vertexShader, 35713) != 1) {
            System.out.println(GL46.glGetShaderInfoLog(vertexShader));
            this.shaderProgram = -1;
         }

         if (GL46.glGetShaderi(fragmentShader, 35713) != 1) {
            System.out.println(GL46.glGetShaderInfoLog(fragmentShader));
            this.shaderProgram = -1;
         }
      }

      if (this.ssbo == -1) {
         this.ssbo = GL46.glGenBuffers();
         GL46.glBindBuffer(37074, this.ssbo);
         GL46.glBufferData(37074, 4800000L, 35048);
      }

      if (this.vao == -1) {
         this.vao = GL46.glGenVertexArrays();
         GL46.glBindVertexArray(this.vao);
         int vbo = GL46.glGenBuffers();
         GL46.glBindBuffer(34962, vbo);
         int[] vboData = new int[400000];
         int i = 0;

         while (i < vboData.length) {
            vboData[i] = i++;
         }

         GL46.glBufferData(34962, vboData, 35045);
         i = GL46.glGenBuffers();
         GL46.glBindBuffer(34963, i);
         int[] eboData = new int[600000];

         for (int ix = 0; ix < 100000; ix++) {
            eboData[ix * 6] = ix * 4;
            eboData[ix * 6 + 1] = ix * 4 + 1;
            eboData[ix * 6 + 2] = ix * 4 + 2;
            eboData[ix * 6 + 3] = ix * 4 + 3;
            eboData[ix * 6 + 4] = ix * 4 + 2;
            eboData[ix * 6 + 5] = ix * 4 + 1;
         }

         GL46.glBufferData(34963, eboData, 35045);
         GL46.glVertexAttribIPointer(0, 4, 5124, 4, 0L);
         GL46.glEnableVertexAttribArray(0);
         GL46.glBindBufferBase(37074, 0, this.ssbo);
         GL46.glBindVertexArray(0);
      }
   }
}
