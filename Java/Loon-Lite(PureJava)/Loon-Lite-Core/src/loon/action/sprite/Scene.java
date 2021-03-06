/**
 * Copyright 2008 - 2015 The Loon Game Engine Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loon
 * @author cping
 * @email：javachenpeng@yahoo.com
 * @version 0.5
 */
package loon.action.sprite;

import loon.opengl.GLEx;

/**
 * 由Entity产生的窗体Scene,方便递归调用
 */
public class Scene extends Entity {

	private long secondsElapsedTotal;

	protected Scene _parentScene;
	protected Scene _childScene;
	private boolean _childSceneModalDraw;
	private boolean _childSceneModalUpdate;

	private boolean mBackgroundEnabled = true;

	public Scene() {
		this(0);
	}

	public Scene(final int count) {
		for (int i = 0; i < count; i++) {
			this.addChild(new Entity());
		}
	}

	public float getSecondsElapsedTotal() {
		return this.secondsElapsedTotal;
	}

	private void setParentScene(final Scene pParentScene) {
		this._parentScene = pParentScene;
	}

	@Override
	public void reset() {
		super.reset();
		this.clearChildScene();
	}

	@Override
	protected void onManagedPaint(final GLEx gl, float offsetX, float offsetY) {
		final Scene childScene = this._childScene;
		if (childScene == null || !this._childSceneModalDraw) {
			if (this.mBackgroundEnabled) {
				super.onManagedPaint(gl, offsetX, offsetY);
			}
		}
		if (childScene != null) {
			childScene.createUI(gl, offsetX, offsetY);
		}
	}

	public Scene back() {
		this.clearChildScene();
		if (this._parentScene != null) {
			this._parentScene.clearChildScene();
			this._parentScene = null;
		}
		return this;
	}

	public boolean hasChildScene() {
		return this._childScene != null;
	}

	public Scene getChildScene() {
		return this._childScene;
	}

	public Scene setChildSceneModal(final Scene child) {
		return this.setChildScene(child, true, true);
	}

	public Scene setChildScene(final Scene child) {
		return this.setChildScene(child, false, false);
	}

	public Scene setChildScene(final Scene child, final boolean modalDraw, final boolean modalUpdate) {
		child.setParentScene(this);
		this._childScene = child;
		this._childSceneModalDraw = modalDraw;
		this._childSceneModalUpdate = modalUpdate;
		return this;
	}

	public Scene clearChildScene() {
		this._childScene = null;
		return this;
	}

	@Override
	protected void onManagedUpdate(final long elapsedTime) {
		this.secondsElapsedTotal += elapsedTime;
		final Scene childScene = this._childScene;
		if (childScene == null || !this._childSceneModalUpdate) {
			super.onManagedUpdate(elapsedTime);
		}
		if (childScene != null) {
			childScene.update(elapsedTime);
		}
	}

}
