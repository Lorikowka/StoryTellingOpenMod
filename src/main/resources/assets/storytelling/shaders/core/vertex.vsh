#version 150

in vec3 Position;
in vec2 UV0;

out vec2 fragUV;
uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    fragUV = UV0;
}
