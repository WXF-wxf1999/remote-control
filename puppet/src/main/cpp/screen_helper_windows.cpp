
/*
    just for windows, generator the dll according to this cpp file,
    and put it in the "lib" directory, it is nearly four times faster tha Java API
*/
#include <atlimage.h>

int g_width = 0;
int g_height = 0;f

void _stdcall setScreenSize(int width, int height) {

	g_width = width;
	g_height = height;
}

int _stdcall screenCapture(char* buffer) {

	HDC hdc = GetDC(NULL);
	int per_bit_pixel = GetDeviceCaps(hdc, BITSPIXEL);

	CImage image;
	image.Create(g_width, g_height, per_bit_pixel);

    // get cursor information
	CURSORINFO cursor_info;
	cursor_info.cbSize = sizeof(CURSORINFO);
	GetCursorInfo(&cursor_info);
	POINT point = cursor_info.ptScreenPos;

    // get cursor icon
	ICONINFO iconinfo;
	GetIconInfo(cursor_info.hCursor, &iconinfo);

	HDC cursor_white_hdc = CreateCompatibleDC(hdc);
	HDC cursor_black_hdc = CreateCompatibleDC(hdc);

	SelectObject(cursor_white_hdc, iconinfo.hbmMask);
	SelectObject(cursor_black_hdc, iconinfo.hbmColor);

    // get screen
	BitBlt(image.GetDC(), 0, 0, g_width, g_height, hdc, 0, 0, SRCCOPY);

    // draw the cursor on the screen
	BitBlt(image.GetDC(), point.x, point.y, 20, 20, cursor_white_hdc, 0, 0, SRCAND);
	BitBlt(image.GetDC(), point.x, point.y, 20, 20, cursor_black_hdc, 0, 0, SRCPAINT);//将带有鼠标的屏幕位图抓取

	ReleaseDC(NULL, hdc);
	image.ReleaseDC();
	image.ReleaseDC();
	image.ReleaseDC();

	IStream* stream = NULL;

	if (S_OK == CreateStreamOnHGlobal(NULL, TRUE, &stream)) {
		image.Save(stream, Gdiplus::ImageFormatPNG);

		LARGE_INTEGER zero;
		memset(&zero, 0, sizeof(zero));
		stream->Seek(zero, STREAM_SEEK_SET, NULL);

		ULARGE_INTEGER size;
		IStream_Size(stream, &size);

		ULONG read;

	    // read into buffer
		stream->Read(buffer, size.LowPart, &read); // 将图片内存再读出
		stream->Release();
		return size.LowPart;
	}
	return 0;
}