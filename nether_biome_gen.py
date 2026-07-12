import numpy as np
import matplotlib.pyplot as plt
import opensimplex as so

def get_biome(x_, z_):
    warp_x = so.noise2(float(x_) / 128.0, float(z_) / 128.0)
    warp_z = so.noise2((float(x_) + 1000) / 256.0, (float(z_) + 1000) / 256.0)
    value = so.noise2(
        (float(x_) + warp_x * 20) / 128.0,
        (float(z_) + warp_z * 20) / 128.0
    )
    if value > 0:
        return 1 # Soul sand valley
    else:
        return 0 # Hell

def get_biome_colour(biome_id):
    if biome_id == 0:
        return 255, 128, 0
    elif biome_id == 1:
        return 100, 72, 60
    else:
        return 255,255,255

def init(seed, dimension):

    # Create the simplex noise
    so.seed(seed)

    # Define the sample space
    height = 1080
    width = 1920
    biome_array = np.zeros((width, height, 3), dtype=np.uint8)

    for z in range(height):
        for x in range(width):
            biome_array[x,z] = get_biome_colour(get_biome(x, z))

    plt.imshow(biome_array)
    # Remove axes for a pure pixel view
    plt.axis('off')
    plt.xticks(np.arange(-0.5, width, 1))
    plt.yticks(np.arange(-0.5, height, 1))
    plt.show()

seed = 123123
init(seed, 1)