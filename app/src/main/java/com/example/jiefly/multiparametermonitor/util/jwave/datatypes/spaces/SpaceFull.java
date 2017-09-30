/**
 * JWave is distributed under the MIT License (MIT); this file is part of.
 *
 * Copyright (c) 2008-2017 Christian Scheiblich (cscheiblich@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.jiefly.multiparametermonitor.util.jwave.datatypes.spaces;

import com.example.jiefly.multiparametermonitor.util.jwave.datatypes.blocks.Block;
import com.example.jiefly.multiparametermonitor.util.jwave.datatypes.blocks.BlockFull;
import com.example.jiefly.multiparametermonitor.util.jwave.exceptions.JWaveException;

/**
 * A space that uses a full array for storage of Block objects.
 * 
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 16.05.2015 15:57:35
 */
public class SpaceFull extends Space {

  /**
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 16.05.2015 16:00:11
   */
  protected Block[ ] _arrBlocks = null;

  /**
   * A space object with no input; e.g. as a pattern.
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 18:51:54
   */
  public SpaceFull( ) {
    super( );
  } // SpaceFull

  /**
   * Copy constructor for generating the same space (cube) object again.
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 18:53:30
   * @param space
   */
  public SpaceFull( Space space ) {
    super( space );

    try {
      alloc( );
      for( int i = 0; i < _noOfRows; i++ )
        for( int j = 0; j < _noOfCols; j++ )
          for( int k = 0; k < _noOfLvls; k++ )
            set( i, j, k, space.get( i, j, k ) );
    } catch( JWaveException e ) {
      e.printStackTrace( );
    } // try

    // TODO implement more efficient by using instanceof

  } // Space

  /**
   * Constructor setting members for and allocating memory!
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 16.05.2015 15:57:35
   * @param i
   *          from 0 to noOfRows-1
   * @param j
   *          from 0 to noOfCols-1
   * @param k
   *          from 0 to noOfLvls-1
   */
  public SpaceFull( int noOfRows, int noOfCols, int noOfLvls ) {
    super( noOfRows, noOfCols, noOfLvls );
  } // SpaceFull

  /**
   * Configure a space (a cube) as a part of a super space.
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 18:50:59
   * @param offSetRow
   *          the starting position for the row of the space
   * @param offSetCol
   *          the starting position for the column of the space
   * @param offSetLvl
   *          the starting position for the level (height) of the space
   * @param noOfRows
   *          the number of rows
   * @param noOfCols
   *          the number of columns
   * @param noOfLvls
   *          the number of levels (height)
   */
  public SpaceFull( int offSetRow, int offSetCol, int offSetLvl, int noOfRows,
      int noOfCols, int noOfLvls ) {
    super( offSetRow, offSetCol, offSetLvl, noOfRows, noOfCols, noOfLvls );
  } // SpaceFull

  /*
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 15:14:11 (non-Javadoc)
   * @see jwave.datatypes.Super#copy()
   */
  @Override public Space copy( ) {
    return new SpaceFull( this );
  } // copy

  /*
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 15:04:11 (non-Javadoc)
   * @see jwave.datatypes.Super#isAllocated()
   */
  @Override public boolean isAllocated( ) {
    boolean isAllocated = true;
    if( _arrBlocks == null )
      isAllocated = false;
    return isAllocated;
  } // isAllocated

  /*
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 15:04:11 (non-Javadoc)
   * @see jwave.datatypes.Super#alloc()
   */
  @Override public void alloc( ) throws JWaveException {
    if( !isAllocated( ) ) {
      _arrBlocks = new Block[ _noOfLvls ];
      for( int k = 0; k < _noOfLvls; k++ ) {
        Block block =
            new BlockFull( _offSetRow, _offSetCol, _noOfRows, _noOfCols );
        block.alloc( );
        _arrBlocks[ k ] = block;
      } // for
    }
  } // alloc

  /*
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 24.05.2015 15:04:11 (non-Javadoc)
   * @see jwave.datatypes.Super#erase()
   */
  @Override public void erase( ) throws JWaveException {
    if( _arrBlocks != null ) {
      for( int k = 0; k < _noOfLvls; k++ ) {
        if( _arrBlocks[ k ] != null ) {
          Block block = _arrBlocks[ k ];
          block.erase( );
          _arrBlocks[ k ] = null;
        } // if
      } // for
      _arrBlocks = null;
    } // if
  } // erase

  /*
   * Getter!
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 16.05.2015 15:57:35 (non-Javadoc)
   * @see jwave.datatypes.spaces.Space#get(int, int, int)
   */
  @Override public double get( int i, int j, int k ) throws JWaveException {
    checkMemory( );
    check( i, j, k );
    Block block = _arrBlocks[ k ];
    double value = block.get( i, j );
    return value;
  } // get

  /*
   * Setter!
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 16.05.2015 15:57:35 (non-Javadoc)
   * @see jwave.datatypes.spaces.Space#set(int, int, int, double)
   */
  @Override public void set( int i, int j, int k, double value )
      throws JWaveException {
    checkMemory( );
    check( i, j, k );
    Block block = _arrBlocks[ k ];
    block.set( i, j, value );
  } // set

} // class
