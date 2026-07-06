package net.minecraft.nbt;

import com.google.common.collect.UnmodifiableIterator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public final class NBTUtil
{
    /**
     * Reads a blockstate from the given tag.
     *
     * @param tag The tag the blockstate is to be read from
     */
    public static IBlockState readBlockState(NBTTagCompound tag)
    {
        if (!tag.hasKey("Name", 8))
        {
            return Blocks.air.getDefaultState();
        }
        else
        {
            Block block = Block.blockRegistry.getObject(new ResourceLocation(tag.getString("Name")));
            IBlockState iblockstate = block.getDefaultState();

            if (tag.hasKey("Properties", 10))
            {
                NBTTagCompound nbttagcompound = tag.getCompoundTag("Properties");
                BlockState blockstatecontainer = block.getBlockState();

                for (String s : nbttagcompound.getKeySet())
                {
                    IProperty<?> iproperty = blockstatecontainer.getProperty();

                    if (iproperty != null)
                    {
                        iblockstate = func_193590_a(iblockstate, iproperty, s, nbttagcompound, tag);
                    }
                }
            }

            return iblockstate;
        }
    }

    private static <T extends Comparable<T>> IBlockState func_193590_a(IBlockState p_193590_0_, IProperty<T> p_193590_1_, String p_193590_2_, NBTTagCompound p_193590_3_, NBTTagCompound p_193590_4_)
    {
        Optional<T> optional = p_193590_1_.parseValue(p_193590_3_.getString(p_193590_2_));

        if (optional.isPresent())
        {
            return p_193590_0_.withProperty(p_193590_1_, optional.get());
        }
        else
        {
            field_193591_a.warn("Unable to read property: {} with value: {} for blockstate: {}", p_193590_2_, p_193590_3_.getString(p_193590_2_), p_193590_4_.toString());
            return p_193590_0_;
        }
    }


    /**
     * Writes the given blockstate to the given tag.
     *
     * @param tag The tag to write to
     * @param state The blockstate to be written
     */
    public static NBTTagCompound writeBlockState(NBTTagCompound tag, IBlockState state)
    {
        tag.setString("Name", ((ResourceLocation)Block.blockRegistry.getNameForObject(state.getBlock())).toString());

        if (!state.getProperties().isEmpty())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            UnmodifiableIterator unmodifiableiterator = state.getProperties().entrySet().iterator();

            while (unmodifiableiterator.hasNext())
            {
                Map.Entry< IProperty<?>, Comparable<? >> entry = (Map.Entry)unmodifiableiterator.next();
                IProperty<?> iproperty = entry.getKey();
                nbttagcompound.setString(iproperty.getName(), getName(iproperty, entry.getValue()));
            }

            tag.setTag("Properties", nbttagcompound);
        }

        return tag;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getName(IProperty<T> p_190010_0_, Comparable<?> p_190010_1_)
    {
        return p_190010_0_.getName((T)p_190010_1_);
    }

    /**
     * Reads and returns a GameProfile that has been saved to the passed in NBTTagCompound
     */
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound)
    {
        String s = null;
        String s1 = null;

        if (compound.hasKey("Name", 8))
        {
            s = compound.getString("Name");
        }

        if (compound.hasKey("Id", 8))
        {
            s1 = compound.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s1))
        {
            return null;
        }
        else
        {
            UUID uuid;

            try
            {
                uuid = UUID.fromString(s1);
            }
            catch (Throwable var12)
            {
                uuid = null;
            }

            GameProfile gameprofile = new GameProfile(uuid, s);

            if (compound.hasKey("Properties", 10))
            {
                NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");

                for (String s2 : nbttagcompound.getKeySet())
                {
                    NBTTagList nbttaglist = nbttagcompound.getTagList(s2, 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound1.getString("Value");

                        if (nbttagcompound1.hasKey("Signature", 8))
                        {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound1.getString("Signature")));
                        }
                        else
                        {
                            gameprofile.getProperties().put(s2, new Property(s2, s3));
                        }
                    }
                }
            }

            return gameprofile;
        }
    }

    /**
     * Writes a GameProfile to an NBTTagCompound.
     */
    public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile)
    {
        if (!StringUtils.isNullOrEmpty(profile.getName()))
        {
            tagCompound.setString("Name", profile.getName());
        }

        if (profile.getId() != null)
        {
            tagCompound.setString("Id", profile.getId().toString());
        }

        if (!profile.getProperties().isEmpty())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            for (String s : profile.getProperties().keySet())
            {
                NBTTagList nbttaglist = new NBTTagList();

                for (Property property : profile.getProperties().get(s))
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setString("Value", property.getValue());

                    if (property.hasSignature())
                    {
                        nbttagcompound1.setString("Signature", property.getSignature());
                    }

                    nbttaglist.appendTag(nbttagcompound1);
                }

                nbttagcompound.setTag(s, nbttaglist);
            }

            tagCompound.setTag("Properties", nbttagcompound);
        }

        return tagCompound;
    }

    public static boolean func_181123_a(NBTBase p_181123_0_, NBTBase p_181123_1_, boolean p_181123_2_)
    {
        if (p_181123_0_ == p_181123_1_)
        {
            return true;
        }
        else if (p_181123_0_ == null)
        {
            return true;
        }
        else if (p_181123_1_ == null)
        {
            return false;
        }
        else if (!p_181123_0_.getClass().equals(p_181123_1_.getClass()))
        {
            return false;
        }
        else if (p_181123_0_ instanceof NBTTagCompound)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)p_181123_0_;
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)p_181123_1_;

            for (String s : nbttagcompound.getKeySet())
            {
                NBTBase nbtbase1 = nbttagcompound.getTag(s);

                if (!func_181123_a(nbtbase1, nbttagcompound1.getTag(s), p_181123_2_))
                {
                    return false;
                }
            }

            return true;
        }
        else if (p_181123_0_ instanceof NBTTagList && p_181123_2_)
        {
            NBTTagList nbttaglist = (NBTTagList)p_181123_0_;
            NBTTagList nbttaglist1 = (NBTTagList)p_181123_1_;

            if (nbttaglist.tagCount() == 0)
            {
                return nbttaglist1.tagCount() == 0;
            }
            else
            {
                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    NBTBase nbtbase = nbttaglist.get(i);
                    boolean flag = false;

                    for (int j = 0; j < nbttaglist1.tagCount(); ++j)
                    {
                        if (func_181123_a(nbtbase, nbttaglist1.get(j), p_181123_2_))
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag)
                    {
                        return false;
                    }
                }

                return true;
            }
        }
        else
        {
            return p_181123_0_.equals(p_181123_1_);
        }
    }
}
