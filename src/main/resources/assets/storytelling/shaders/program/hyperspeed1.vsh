//hyperspeed1.vsh
#version 150

in vec4 Position;

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;

out vec2 fragUV;
out vec2 oneTexel;

void main() {
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    fragUV = Position.xy / OutSize;
//    oneTexel = 1.0 / InSize;
//    fragUV = outPos.xy * 0.5 + 0.5;
}