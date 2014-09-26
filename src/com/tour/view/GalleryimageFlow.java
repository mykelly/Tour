package com.tour.view;


import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryimageFlow extends Gallery {

    private Camera mCamera = new Camera();//�����
    private int mMaxRotationAngle = 60;//���ת���Ƕ�
    private int mMaxZoom = -150;//���ű�{imagViewռGalleryFlow�ؼ��ı���}
    private int mCoveflowCenter;//�뾶ֵ
    private int rotation;
//	private int mCurrentBufferIndex;
//	private int mCurrentAdapterIndex;
//	private int mNextScreen = INVALID_SCREEN;
    protected boolean checkLayoutParams(LayoutParams p) {
		// TODO Auto-generated method stub
		return super.checkLayoutParams(p);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void setGravity(int gravity) {
		// TODO Auto-generated method stub
		super.setGravity(gravity);
	}

	public GalleryimageFlow(Context context) {
            super(context);
            this.setStaticTransformationsEnabled(true);//֧��ת�� ,ִ��getChildStaticTransformation����

    }

    public GalleryimageFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }

    public GalleryimageFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }

    public int getMaxRotationAngle() {
            return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
            mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
            return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
            mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }

     //�������û����������
	/*public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
//		return super.onFling(e1, e2, velocityX, velocityY);
    	return false;
	}*/

	private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }
    
  //����gallery��ÿ��ͼƬ����ת(��д��gallery�з���)
    protected boolean getChildStaticTransformation(View child, Transformation t) {
    	//ȡ�õ�ǰ��view�İ뾶ֵ
            final int childCenter = getCenterOfView(child);
            final int childWidth = child.getWidth();
            //��ת�Ƕ�
            int rotationAngle = 0;
          //����ת��״̬
            t.clear();
          //����ת������
            t.setTransformationType(Transformation.TYPE_MATRIX);
          //���ͼƬλ������λ�ò���Ҫ������ת
            if (childCenter == mCoveflowCenter) {
                transformImageBitmap((ImageView) child, t, 0);
            } else {
            	//����ͼƬ��gallery�е�λ��������ͼƬ����ת�Ƕ�
                rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
                //�����ת�ǶȾ���ֵ���������ת�Ƕȷ���(-mMaxRotationAngle��mMaxRotationAngle;)
                if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                    rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
                }
                transformImageBitmap((ImageView) child, t, rotationAngle);
            }
            return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }

    private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
    	//��Ч�����б���
            mCamera.save();
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height; //ͼƬ�߶�
            final int imageWidth = child.getLayoutParams().width;//ͼƬ���
            rotation = Math.abs(rotationAngle);//������ת�Ƕȵľ���ֵ

            // ��Z���������ƶ�camera���ӽǣ�ʵ��Ч��Ϊ�Ŵ�ͼƬ��
            // �����Y�����ƶ�����ͼƬ�����ƶ���X���϶�ӦͼƬ�����ƶ���
            mCamera.translate(0.0f, 0.0f, 100.0f);
     
    		 //As the angle of the view gets less, zoom in
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));//�Ŵ���
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }
  			                   
            // ��Y������ת����ӦͼƬ�������﷭ת��
            // �����X������ת�����ӦͼƬ�������﷭ת��
        //    mCamera.rotateY(rotationAngle);
       //     mCamera.rotateY(-rotationAngle);
     //       mCamera.rotateX(rotationAngle);
     //       mCamera.rotateZ(rotationAngle);
      //      mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
            mCamera.restore();
    }
   
	
}
